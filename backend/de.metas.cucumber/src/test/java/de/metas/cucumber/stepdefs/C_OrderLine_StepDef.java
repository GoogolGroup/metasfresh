/*
 * #%L
 * de.metas.cucumber
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

package de.metas.cucumber.stepdefs;

import de.metas.common.util.Check;
import de.metas.contracts.model.I_C_Flatrate_Conditions;
import de.metas.contracts.model.I_C_Flatrate_Term;
import de.metas.cucumber.stepdefs.attribute.M_AttributeSetInstance_StepDefData;
import de.metas.cucumber.stepdefs.contract.C_Flatrate_Conditions_StepDefData;
import de.metas.cucumber.stepdefs.contract.C_Flatrate_Term_StepDefData;
import de.metas.cucumber.stepdefs.hu.M_HU_PI_Item_Product_StepDefData;
import de.metas.cucumber.stepdefs.pricing.C_TaxCategory_StepDefData;
import de.metas.currency.Currency;
import de.metas.currency.CurrencyCode;
import de.metas.currency.ICurrencyDAO;
import de.metas.handlingunits.model.I_M_HU_PI_Item_Product;
import de.metas.ordercandidate.model.I_C_OLCand;
import de.metas.uom.IUOMDAO;
import de.metas.uom.UomId;
import de.metas.uom.X12DE355;
import de.metas.util.Services;
import de.metas.util.StringUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.NonNull;
import org.adempiere.ad.dao.IQueryBL;
import org.adempiere.model.InterfaceWrapperHelper;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_DocType;
import org.compiere.model.I_C_Order;
import org.compiere.model.I_C_OrderLine;
import org.compiere.model.I_C_TaxCategory;
import org.compiere.model.I_M_AttributeSetInstance;
import org.compiere.model.I_M_Product;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static de.metas.cucumber.stepdefs.StepDefConstants.TABLECOLUMN_IDENTIFIER;
import static org.adempiere.model.InterfaceWrapperHelper.newInstance;
import static org.adempiere.model.InterfaceWrapperHelper.saveRecord;
import static org.assertj.core.api.Assertions.*;
import static org.compiere.model.I_C_OrderLine.COLUMNNAME_M_Product_ID;
import static org.compiere.model.I_C_TaxCategory.COLUMNNAME_C_TaxCategory_ID;

public class C_OrderLine_StepDef
{
	private final IQueryBL queryBL = Services.get(IQueryBL.class);
	private final ICurrencyDAO currencyDAO = Services.get(ICurrencyDAO.class);
	private final IUOMDAO uomDAO = Services.get(IUOMDAO.class);

	private final M_Product_StepDefData productTable;
	private final C_BPartner_StepDefData partnerTable;
	private final C_Order_StepDefData orderTable;
	private final C_OrderLine_StepDefData orderLineTable;
	private final M_AttributeSetInstance_StepDefData attributeSetInstanceTable;
	private final C_Flatrate_Conditions_StepDefData flatrateConditionsTable;
	private final C_Flatrate_Term_StepDefData contractTable;
	private final C_TaxCategory_StepDefData taxCategoryTable;

	private final M_HU_PI_Item_Product_StepDefData huPiItemProductTable;

	public C_OrderLine_StepDef(
			@NonNull final M_Product_StepDefData productTable,
			@NonNull final C_BPartner_StepDefData partnerTable,
			@NonNull final C_Order_StepDefData orderTable,
			@NonNull final C_OrderLine_StepDefData orderLineTable,
			@NonNull final M_AttributeSetInstance_StepDefData attributeSetInstanceTable,
			@NonNull final C_Flatrate_Conditions_StepDefData flatrateConditionsTable,
			@NonNull final C_Flatrate_Term_StepDefData contractTable,
			@NonNull final C_TaxCategory_StepDefData taxCategoryTable,
			@NonNull final M_HU_PI_Item_Product_StepDefData huPiItemProductTable)
	{
		this.productTable = productTable;
		this.partnerTable = partnerTable;
		this.orderTable = orderTable;
		this.orderLineTable = orderLineTable;
		this.attributeSetInstanceTable = attributeSetInstanceTable;
		this.flatrateConditionsTable = flatrateConditionsTable;
		this.contractTable = contractTable;
		this.taxCategoryTable = taxCategoryTable;
		this.huPiItemProductTable = huPiItemProductTable;
	}

	@Given("metasfresh contains C_OrderLines:")
	public void metasfresh_contains_c_order_lines(@NonNull final DataTable dataTable)
	{
		final List<Map<String, String>> tableRows = dataTable.asMaps(String.class, String.class);
		for (final Map<String, String> tableRow : tableRows)
		{
			final I_C_OrderLine orderLine = newInstance(I_C_OrderLine.class);
			orderLine.setAD_Org_ID(StepDefConstants.ORG_ID.getRepoId());

			final String productIdentifier = DataTableUtil.extractStringForColumnName(tableRow, COLUMNNAME_M_Product_ID + ".Identifier");
			final Integer productId = productTable.getOptional(productIdentifier)
					.map(I_M_Product::getM_Product_ID)
					.orElseGet(() -> Integer.parseInt(productIdentifier));

			orderLine.setM_Product_ID(productId);
			orderLine.setQtyEntered(DataTableUtil.extractBigDecimalForColumnName(tableRow, I_C_OrderLine.COLUMNNAME_QtyEntered));

			final String attributeSetInstanceIdentifier = DataTableUtil.extractStringOrNullForColumnName(tableRow, "OPT." + I_C_OrderLine.COLUMNNAME_M_AttributeSetInstance_ID + "." + TABLECOLUMN_IDENTIFIER);
			if (Check.isNotBlank(attributeSetInstanceIdentifier))
			{
				final I_M_AttributeSetInstance attributeSetInstance = attributeSetInstanceTable.get(attributeSetInstanceIdentifier);
				assertThat(attributeSetInstance).isNotNull();

				orderLine.setM_AttributeSetInstance_ID(attributeSetInstance.getM_AttributeSetInstance_ID());
			}

			final String orderIdentifier = DataTableUtil.extractStringForColumnName(tableRow, I_C_OrderLine.COLUMNNAME_C_Order_ID + "." + TABLECOLUMN_IDENTIFIER);
			final I_C_Order order = orderTable.get(orderIdentifier);
			orderLine.setC_Order_ID(order.getC_Order_ID());

			final String partnerIdentifier = DataTableUtil.extractStringOrNullForColumnName(tableRow, "OPT." + I_C_OrderLine.COLUMNNAME_C_BPartner_ID + ".Identifier");
			if (partnerIdentifier != null)
			{
				final I_C_BPartner partner = partnerTable.get(partnerIdentifier);
				orderLine.setC_BPartner_ID(partner.getC_BPartner_ID());
			}

			final String flatrateConditionsIdentifier = DataTableUtil.extractStringOrNullForColumnName(tableRow, "OPT." + I_C_OrderLine.COLUMNNAME_C_Flatrate_Conditions_ID + "." + TABLECOLUMN_IDENTIFIER);
			if (Check.isNotBlank(flatrateConditionsIdentifier))
			{
				final I_C_Flatrate_Conditions flatrateConditions = flatrateConditionsTable.get(flatrateConditionsIdentifier);
				orderLine.setC_Flatrate_Conditions_ID(flatrateConditions.getC_Flatrate_Conditions_ID());
			}

			saveRecord(orderLine);

			orderLineTable.putOrReplace(DataTableUtil.extractRecordIdentifier(tableRow, I_C_OrderLine.COLUMNNAME_C_OrderLine_ID), orderLine);
		}
	}

	@Then("the purchase order with document subtype {string} linked to order {string} has lines:")
	public void thePurchaseOrderLinkedToOrderO_HasLines(@Nullable final String docSubType, @NonNull final String linkedOrderIdentifier, @NonNull final DataTable dataTable)
	{
		final I_C_Order purchaseOrder = queryBL
				.createQueryBuilder(I_C_Order.class)
				.addOnlyActiveRecordsFilter()
				.addEqualsFilter(I_C_Order.COLUMNNAME_Link_Order_ID, orderTable.get(linkedOrderIdentifier).getC_Order_ID())
				.create().firstOnly(I_C_Order.class);

		assertThat(purchaseOrder).isNotNull();

		final I_C_DocType docType = queryBL
				.createQueryBuilder(I_C_DocType.class)
				.addOnlyActiveRecordsFilter()
				.addEqualsFilter(I_C_DocType.COLUMN_C_DocType_ID, purchaseOrder.getC_DocTypeTarget_ID())
				.create().firstOnly(I_C_DocType.class);

		assertThat(docType).isNotNull();
		if (Check.isNotBlank(docSubType))
		{
			assertThat(docType.getDocSubType()).isEqualTo(docSubType);
		}

		final List<I_C_OrderLine> purchaseOrderLines = queryBL
				.createQueryBuilder(I_C_OrderLine.class)
				.addOnlyActiveRecordsFilter()
				.addEqualsFilter(I_C_OrderLine.COLUMNNAME_C_Order_ID, purchaseOrder.getC_Order_ID())
				.create()
				.list(I_C_OrderLine.class);

		final List<Map<String, String>> tableRows = dataTable.asMaps(String.class, String.class);
		for (final Map<String, String> tableRow : tableRows)
		{
			final BigDecimal qtyOrdered = DataTableUtil.extractBigDecimalForColumnName(tableRow, I_C_OrderLine.COLUMNNAME_QtyOrdered);
			final BigDecimal netAmt = DataTableUtil.extractBigDecimalForColumnName(tableRow, I_C_OrderLine.COLUMNNAME_LineNetAmt);
			final String productIdentifier = DataTableUtil.extractStringForColumnName(tableRow, COLUMNNAME_M_Product_ID + ".Identifier");
			final String partnerIdentifier = DataTableUtil.extractStringOrNullForColumnName(tableRow, "OPT." + I_C_OrderLine.COLUMNNAME_C_BPartner_ID + ".Identifier");
			final int partnerId = Check.isBlank(partnerIdentifier) ? 0 : partnerTable.get(partnerIdentifier).getC_BPartner_ID();

			boolean linePresent = false;

			for (final I_C_OrderLine orderLine : purchaseOrderLines)
			{
				linePresent = orderLine.getLineNetAmt().compareTo(netAmt) == 0
						&& orderLine.getQtyOrdered().compareTo(qtyOrdered) == 0
						&& orderLine.getM_Product_ID() == productTable.get(productIdentifier).getM_Product_ID();
				if (partnerId > 0)
				{
					linePresent = linePresent && orderLine.getC_BPartner_ID() == partnerId;
				}

				if (linePresent)
				{
					break;
				}
			}

			assertThat(linePresent).isTrue();
		}
	}

	@And("validate the created order lines")
	public void validate_created_order_lines(@NonNull final DataTable table)
	{
		final List<Map<String, String>> dataTable = table.asMaps();
		for (final Map<String, String> row : dataTable)
		{
			final String orderIdentifier = DataTableUtil.extractStringForColumnName(row, "C_Order_ID.Identifier");

			final I_C_Order orderRecord = orderTable.get(orderIdentifier);

			final String productIdentifier = DataTableUtil.extractStringForColumnName(row, I_C_OLCand.COLUMNNAME_M_Product_ID + "." + StepDefConstants.TABLECOLUMN_IDENTIFIER);
			final Integer expectedProductId = productTable.getOptional(productIdentifier)
					.map(I_M_Product::getM_Product_ID)
					.orElseGet(() -> Integer.parseInt(productIdentifier));

			final BigDecimal qtyOrdered = DataTableUtil.extractBigDecimalForColumnName(row, I_C_OrderLine.COLUMNNAME_QtyOrdered);

			//dev-note: we assume the tests are not using the same product on different lines
			final I_C_OrderLine orderLineRecord = queryBL.createQueryBuilder(I_C_OrderLine.class)
					.addEqualsFilter(I_C_OrderLine.COLUMNNAME_C_Order_ID, orderRecord.getC_Order_ID())
					.addEqualsFilter(I_C_OrderLine.COLUMNNAME_M_Product_ID, expectedProductId)
					.addEqualsFilter(I_C_OrderLine.COLUMNNAME_QtyOrdered, qtyOrdered)
					.create()
					.firstOnlyNotNull(I_C_OrderLine.class);

			validateOrderLine(orderLineRecord, row);
		}
	}

	@And("validate C_OrderLine:")
	public void validate_C_OrderLine(@NonNull final DataTable dataTable)
	{
		final List<Map<String, String>> table = dataTable.asMaps();
		for (final Map<String, String> row : table)
		{
			final String orderLineIdentifier = DataTableUtil.extractStringForColumnName(row, I_C_OrderLine.COLUMNNAME_C_OrderLine_ID + "." + TABLECOLUMN_IDENTIFIER);
			final I_C_OrderLine orderLine = orderLineTable.get(orderLineIdentifier);
			assertThat(orderLine).isNotNull();

			InterfaceWrapperHelper.refresh(orderLine);

			validateOrderLine(orderLine, row);
		}
	}

	@And("update C_OrderLine:")
	public void update_C_OrderLine(@NonNull final DataTable dataTable)
	{
		final List<Map<String, String>> table = dataTable.asMaps();
		for (final Map<String, String> row : table)
		{
			final String olIdentifier = DataTableUtil.extractStringForColumnName(row, I_C_OrderLine.COLUMNNAME_C_OrderLine_ID + "." + TABLECOLUMN_IDENTIFIER);
			final de.metas.handlingunits.model.I_C_OrderLine orderLine = InterfaceWrapperHelper.create(orderLineTable.get(olIdentifier), de.metas.handlingunits.model.I_C_OrderLine.class);

			final String contractIdentifier = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_C_Flatrate_Term_ID + "." + TABLECOLUMN_IDENTIFIER);

			if (Check.isNotBlank(contractIdentifier))
			{
				final I_C_Flatrate_Term contract = contractTable.get(contractIdentifier);

				orderLine.setC_Flatrate_Term_ID(contract.getC_Flatrate_Term_ID());
			}

			final BigDecimal updatedQtyEntered = DataTableUtil.extractBigDecimalOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_QtyEntered);
			if (updatedQtyEntered != null)
			{
				orderLine.setQtyEntered(updatedQtyEntered);
			}

			final String piItemProductIdentifier = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + de.metas.handlingunits.model.I_C_OrderLine.COLUMNNAME_M_HU_PI_Item_Product_ID);
			if (Check.isNotBlank(piItemProductIdentifier))
			{
				final Integer piItemProductId = huPiItemProductTable.getOptional(piItemProductIdentifier)
						.map(I_M_HU_PI_Item_Product::getM_HU_PI_Item_Product_ID)
						.orElseGet(() -> Integer.parseInt(piItemProductIdentifier));

				orderLine.setM_HU_PI_Item_Product_ID(piItemProductId);
			}

			saveRecord(orderLine);

			orderLineTable.putOrReplace(olIdentifier, orderLine);
		}
	}

	private void validateOrderLine(@NonNull final I_C_OrderLine orderLine, @NonNull final Map<String, String> row)
	{
		final String orderIdentifier = DataTableUtil.extractStringForColumnName(row, "C_Order_ID.Identifier");
		final Timestamp dateOrdered = DataTableUtil.extractDateTimestampForColumnNameOrNull(row, "OPT." + I_C_OrderLine.COLUMNNAME_DateOrdered);
		final String productIdentifier = DataTableUtil.extractStringForColumnName(row, I_C_OrderLine.COLUMNNAME_M_Product_ID + ".Identifier");
		final BigDecimal qtyDelivered = DataTableUtil.extractBigDecimalForColumnName(row, "qtydelivered");
		final BigDecimal qtyordered = DataTableUtil.extractBigDecimalForColumnName(row, I_C_OrderLine.COLUMNNAME_QtyOrdered);
		final BigDecimal qtyinvoiced = DataTableUtil.extractBigDecimalForColumnName(row, "qtyinvoiced");
		final BigDecimal price = DataTableUtil.extractBigDecimalWithScaleForColumnName(row, "price");
		final BigDecimal discount = DataTableUtil.extractBigDecimalForColumnName(row, "discount");
		final String currencyCode = DataTableUtil.extractStringForColumnName(row, "currencyCode");
		final boolean processed = DataTableUtil.extractBooleanForColumnName(row, "processed");
		final String taxCategoryIdentifier = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + COLUMNNAME_C_TaxCategory_ID + "." + TABLECOLUMN_IDENTIFIER);

		final Integer expectedProductId = productTable.getOptional(productIdentifier)
				.map(I_M_Product::getM_Product_ID)
				.orElseGet(() -> Integer.parseInt(productIdentifier));

		final String uomBPartner355Code = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_C_UOM_BPartner_ID + "." + X12DE355.class.getSimpleName());

		if (Check.isNotBlank(uomBPartner355Code))
		{
			final UomId bPartnerUOMId = uomDAO.getUomIdByX12DE355(X12DE355.ofCode(uomBPartner355Code));
			assertThat(orderLine.getC_UOM_BPartner_ID()).isEqualTo(bPartnerUOMId.getRepoId());
		}

		final String isManualPriceStr = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_IsManualPrice);

		if (Check.isNotBlank(isManualPriceStr))
		{
			final boolean isManualPrice = StringUtils.toBoolean(isManualPriceStr);
			assertThat(orderLine.isManualPrice()).isEqualTo(isManualPrice);
		}

		final String bPartnerQtyItemCapacity = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_BPartner_QtyItemCapacity);

		if (Check.isNotBlank(bPartnerQtyItemCapacity))
		{
			assertThat(orderLine.getBPartner_QtyItemCapacity()).isEqualByComparingTo(bPartnerQtyItemCapacity);
		}

		final BigDecimal qtyEnteredInBPartnerUOM = DataTableUtil.extractBigDecimalOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_QtyEnteredInBPartnerUOM);
		if (qtyEnteredInBPartnerUOM != null)
		{
			assertThat(orderLine.getQtyEnteredInBPartnerUOM()).isEqualByComparingTo(qtyEnteredInBPartnerUOM);
		}

		final BigDecimal qtyEntered = DataTableUtil.extractBigDecimalOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_QtyEntered);
		if (qtyEntered != null)
		{
			assertThat(orderLine.getQtyEntered()).isEqualByComparingTo(qtyEntered);
		}

		final String uomCode = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_C_UOM_ID + "." + X12DE355.class.getSimpleName());

		if (Check.isNotBlank(uomCode))
		{
			final UomId uomId = uomDAO.getUomIdByX12DE355(X12DE355.ofCode(uomCode));
			assertThat(orderLine.getC_UOM_ID()).isEqualTo(uomId.getRepoId());
		}

		final BigDecimal qtyItemCapacity = DataTableUtil.extractBigDecimalOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_QtyItemCapacity);

		if (qtyItemCapacity != null)
		{
			assertThat(orderLine.getQtyItemCapacity()).isEqualByComparingTo(qtyItemCapacity);
		}

		if (dateOrdered != null)
		{
			assertThat(orderLine.getDateOrdered()).as("DateOrdered").isEqualTo(dateOrdered);
		}

		if (Check.isNotBlank(taxCategoryIdentifier))
		{
			final Integer taxCategoryId = taxCategoryTable.getOptional(taxCategoryIdentifier)
					.map(I_C_TaxCategory::getC_TaxCategory_ID)
					.orElseGet(() -> Integer.parseInt(taxCategoryIdentifier));

			assertThat(orderLine.getC_TaxCategory_ID()).as("C_TaxCategory_ID").isEqualTo(taxCategoryId);
		}

		assertThat(orderLine.getC_Order_ID()).as("C_Order_ID").isEqualTo(orderTable.get(orderIdentifier).getC_Order_ID());
		assertThat(orderLine.getQtyDelivered()).as("QtyDelivered").isEqualTo(qtyDelivered);
		assertThat(orderLine.getPriceEntered()).as("PriceEntered").isEqualTo(price);
		assertThat(orderLine.getDiscount()).as("Discount").isEqualTo(discount);
		assertThat(orderLine.isProcessed()).as("Processed").isEqualTo(processed);
		assertThat(orderLine.getM_Product_ID()).as("M_Product_ID").isEqualTo(expectedProductId);
		assertThat(orderLine.getQtyOrdered()).as("QtyOrdered").isEqualByComparingTo(qtyordered);
		assertThat(orderLine.getQtyInvoiced()).as("QtyInvoiced").isEqualTo(qtyinvoiced);

		final Currency currency = currencyDAO.getByCurrencyCode(CurrencyCode.ofThreeLetterCode(currencyCode));
		assertThat(orderLine.getC_Currency_ID()).isEqualTo(currency.getId().getRepoId());

		final String flatrateConditionsIdentifier = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_C_Flatrate_Conditions_ID + "." + TABLECOLUMN_IDENTIFIER);
		if (Check.isNotBlank(flatrateConditionsIdentifier))
		{
			final I_C_Flatrate_Conditions flatrateConditions = flatrateConditionsTable.get(flatrateConditionsIdentifier);
			assertThat(orderLine.getC_Flatrate_Conditions_ID()).isEqualTo(flatrateConditions.getC_Flatrate_Conditions_ID());
		}

		final String x12de355StockCode = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_C_UOM_ID + "." + X12DE355.class.getSimpleName());
		if (Check.isNotBlank(x12de355StockCode))
		{
			final UomId stockUomId = uomDAO.getUomIdByX12DE355(X12DE355.ofCode(x12de355StockCode));
			assertThat(orderLine.getC_UOM_ID()).isEqualTo(stockUomId.getRepoId());
		}

		final String x12de355PriceCode = DataTableUtil.extractStringOrNullForColumnName(row, "OPT." + I_C_OrderLine.COLUMNNAME_Price_UOM_ID + "." + X12DE355.class.getSimpleName());
		if (Check.isNotBlank(x12de355PriceCode))
		{
			final UomId productPriceUomId = uomDAO.getUomIdByX12DE355(X12DE355.ofCode(x12de355PriceCode));
			assertThat(orderLine.getPrice_UOM_ID()).isEqualTo(productPriceUomId.getRepoId());
		}

		final String orderLineIdentifier = DataTableUtil.extractStringForColumnName(row, I_C_OrderLine.COLUMNNAME_C_OrderLine_ID + "." + StepDefConstants.TABLECOLUMN_IDENTIFIER);

		orderLineTable.putOrReplace(orderLineIdentifier, orderLine);
	}
}
