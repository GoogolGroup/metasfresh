/*
 * #%L
 * de.metas.ui.web.base
 * %%
 * Copyright (C) 2021 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package de.metas.ui.web.material.adapter;

import com.google.common.annotations.VisibleForTesting;
import de.metas.material.cockpit.availableforsales.AvailableForSalesMultiQuery;
import de.metas.material.cockpit.availableforsales.AvailableForSalesMultiResult;
import de.metas.material.cockpit.availableforsales.AvailableForSalesRepository;
import de.metas.material.cockpit.availableforsales.AvailableForSalesResult;
import de.metas.material.commons.attributes.AttributesKeyPattern;
import de.metas.material.event.commons.AttributesKey;
import de.metas.product.IProductBL;
import de.metas.quantity.Quantity;
import de.metas.ui.web.material.adapter.AvailabilityInfoResultForWebui.AvailabilityInfoResultForWebuiBuilder;
import de.metas.util.Services;
import lombok.NonNull;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.mm.attributes.api.AttributesKeys;
import org.adempiere.mm.attributes.api.ImmutableAttributeSet;
import org.compiere.model.I_C_UOM;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class AvailableForSaleAdapter
{
	private final IProductBL productsService = Services.get(IProductBL.class);
	private final AvailableForSalesRepository availableForSalesRepository;

	public AvailableForSaleAdapter(@NonNull final AvailableForSalesRepository stockRepository)
	{
		this.availableForSalesRepository = stockRepository;
	}

	@VisibleForTesting
	static AvailabilityInfoResultForWebui.Group.Type extractGroupType(@NonNull final AttributesKey attributesKey)
	{
		if (AttributesKey.ALL.equals(attributesKey))
		{
			return AvailabilityInfoResultForWebui.Group.Type.ALL_STORAGE_KEYS;
		}
		else if (AttributesKey.OTHER.equals(attributesKey))
		{
			return AvailabilityInfoResultForWebui.Group.Type.OTHER_STORAGE_KEYS;
		}
		else
		{
			return AvailabilityInfoResultForWebui.Group.Type.ATTRIBUTE_SET;
		}
	}

	@NonNull
	public AvailabilityInfoResultForWebui retrieveAvailableStock(@NonNull final AvailableForSalesMultiQuery query)
	{
		final AvailableForSalesMultiResult commonsAvailableStock = availableForSalesRepository.getBy(query);

		final AvailabilityInfoResultForWebuiBuilder clientResultBuilder = AvailabilityInfoResultForWebui.builder();

		final List<AvailableForSalesResult> commonsResultGroups = commonsAvailableStock.getAvailableForSalesResults();
		for (final AvailableForSalesResult commonsResultGroup : commonsResultGroups)
		{
			final AvailabilityInfoResultForWebui.Group clientResultGroup = createClientResultGroup(commonsResultGroup);
			clientResultBuilder.group(clientResultGroup);
		}
		return clientResultBuilder.build();
	}

	private AvailabilityInfoResultForWebui.Group createClientResultGroup(@NonNull final AvailableForSalesResult commonsResultGroup)
	{
		try
		{
			return createClientResultGroup0(commonsResultGroup);
		}
		catch (final RuntimeException e)
		{
			throw AdempiereException.wrapIfNeeded(e).appendParametersToMessage()
					.setParameter("commonsResultGroup", commonsResultGroup);
		}
	}

	private AvailabilityInfoResultForWebui.Group createClientResultGroup0(final AvailableForSalesResult commonsResultGroup)
	{
		final Quantity quantity = extractQuantity(commonsResultGroup);
		final AttributesKey attributesKey = commonsResultGroup.getStorageAttributesKey();
		final AvailabilityInfoResultForWebui.Group.Type type = extractGroupType(attributesKey);

		final ImmutableAttributeSet attributes = AvailabilityInfoResultForWebui.Group.Type.ATTRIBUTE_SET.equals(type)
				? AttributesKeys.toImmutableAttributeSet(attributesKey)
				: ImmutableAttributeSet.EMPTY;

		return AvailabilityInfoResultForWebui.Group.builder()
				.productId(commonsResultGroup.getProductId())
				.qty(quantity)
				.type(type)
				.attributes(attributes)
				.build();
	}

	private Quantity extractQuantity(final AvailableForSalesResult commonsResultGroup)
	{
		final AvailableForSalesResult.Quantities quantities = commonsResultGroup.getQuantities();
		final BigDecimal qty = quantities.getQtyOnHandStock().subtract(quantities.getQtyToBeShipped());
		final I_C_UOM uom = productsService.getStockUOM(commonsResultGroup.getProductId());
		return Quantity.of(qty, uom);
	}

	public Set<AttributesKeyPattern> getPredefinedStorageAttributeKeys()
	{
		return availableForSalesRepository.getPredefinedStorageAttributeKeys();
	}
}