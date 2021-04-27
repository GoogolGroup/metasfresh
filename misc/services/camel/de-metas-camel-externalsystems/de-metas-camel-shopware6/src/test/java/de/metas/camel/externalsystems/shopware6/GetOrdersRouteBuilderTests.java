/*
 * #%L
 * de-metas-camel-shopware6
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

package de.metas.camel.externalsystems.shopware6;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableMap;
import de.metas.camel.externalsystems.common.ExternalSystemCamelConstants;
import de.metas.camel.externalsystems.common.v2.BPUpsertCamelRequest;
import de.metas.camel.externalsystems.shopware6.api.ShopwareClient;
import de.metas.camel.externalsystems.shopware6.api.model.country.JsonCountry;
import de.metas.camel.externalsystems.shopware6.api.model.order.JsonOrderAddress;
import de.metas.camel.externalsystems.shopware6.api.model.order.JsonOrderAddressAndCustomId;
import de.metas.camel.externalsystems.shopware6.api.model.order.JsonOrderAndCustomId;
import de.metas.camel.externalsystems.shopware6.api.model.order.JsonOrderLines;
import de.metas.camel.externalsystems.shopware6.api.model.order.JsonOrders;
import de.metas.camel.externalsystems.shopware6.currency.CurrencyInfoProvider;
import de.metas.camel.externalsystems.shopware6.order.GetOrdersRouteBuilder;
import de.metas.camel.externalsystems.shopware6.order.ImportOrdersRouteContext;
import de.metas.common.ordercandidates.v2.request.JsonOLCandClearRequest;
import de.metas.common.ordercandidates.v2.request.JsonOLCandCreateBulkRequest;
import lombok.NonNull;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import static de.metas.camel.externalsystems.shopware6.Shopware6Constants.ROUTE_PROPERTY_IMPORT_ORDERS_CONTEXT;
import static de.metas.camel.externalsystems.shopware6.ShopwareTestConstants.MOCK_CURRENCY_ID;
import static de.metas.camel.externalsystems.shopware6.ShopwareTestConstants.MOCK_EUR_CODE;
import static de.metas.camel.externalsystems.shopware6.ShopwareTestConstants.MOCK_ORG_CODE;
import static de.metas.camel.externalsystems.shopware6.order.GetOrdersRouteBuilder.CLEAR_ORDERS_ROUTE_ID;
import static de.metas.camel.externalsystems.shopware6.order.GetOrdersRouteBuilder.CREATE_BPARTNER_UPSERT_REQ_PROCESSOR_ID;
import static de.metas.camel.externalsystems.shopware6.order.GetOrdersRouteBuilder.GET_ORDERS_PROCESSOR_ID;
import static de.metas.camel.externalsystems.shopware6.order.GetOrdersRouteBuilder.GET_ORDERS_ROUTE_ID;
import static de.metas.camel.externalsystems.shopware6.order.GetOrdersRouteBuilder.OLCAND_REQ_PROCESSOR_ID;
import static de.metas.camel.externalsystems.shopware6.order.GetOrdersRouteBuilder.PROCESS_ORDER_ROUTE_ID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;

public class GetOrdersRouteBuilderTests extends CamelTestSupport
{
	private static final String MOCK_BPARTNER_UPSERT = "mock:bPartnerUpsert";
	private static final String MOCK_OL_CAND_CREATE = "mock:olCandCreate";
	private static final String MOCK_OL_CAND_CLEAR = "mock:olCandClear";

	private static final String JSON_ORDERS_RESOURCE_PATH = "10_JsonOrders.json";
	private static final String JSON_ORDER_DELIVERIES_PATH = "20_JsonOrderDeliveries.json";
	private static final String JSON_ORDER_BILLING_ADDRESS_PATH = "30_Billing_JsonOrderAddressCustomId.json";
	private static final String JSON_ORDER_LINES = "40_JsonOrderLines.json";
	private static final String JSON_COUNTRY_INFO_PATH = "JsonCountry.json";

	private static final String JSON_UPSERT_BPARTNER_REQUEST = "50_CamelUpsertBPartnerCompositeRequest.json";
	private static final String JSON_UPSERT_BPARTNER_RESPONSE = "50_CamelUpsertBPartnerCompositeResponse.json";

	private static final String JSON_OL_CAND_CREATE_REQUEST = "60_JsonOLCandCreateBulkRequest.json";

	private static final String JSON_OL_CAND_CLEAR_REQUEST = "70_JsonOLCandClearRequest.json";

	@Override
	protected Properties useOverridePropertiesWithPropertiesComponent()
	{
		final var properties = new Properties();
		try
		{
			properties.load(GetOrdersRouteBuilderTests.class.getClassLoader().getResourceAsStream("application.properties"));
			return properties;
		}
		catch (final IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	protected RouteBuilder createRouteBuilder()
	{
		return new GetOrdersRouteBuilder();
	}

	@Override
	public boolean isUseAdviceWith()
	{
		return true;
	}

	@Test
	void happyFlow() throws Exception
	{
		final MockUpsertBPartnerProcessor createdBPartnerProcessor = new MockUpsertBPartnerProcessor();
		final MockSuccessfullyCreatedOLCandProcessor successfullyCreatedOLCandProcessor = new MockSuccessfullyCreatedOLCandProcessor();
		final MockSuccessfullyClearOrdersProcessor successfullyClearOrdersProcessor = new MockSuccessfullyClearOrdersProcessor();

		prepareRouteForTesting(createdBPartnerProcessor, successfullyCreatedOLCandProcessor, successfullyClearOrdersProcessor);

		context.start();

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		// validate BPUpsertCamelRequest
		final InputStream expectedUpsertBPartnerRequestIS = this.getClass().getResourceAsStream(JSON_UPSERT_BPARTNER_REQUEST);

		final MockEndpoint bPartnerMockEndpoint = getMockEndpoint(MOCK_BPARTNER_UPSERT);
		bPartnerMockEndpoint.expectedBodiesReceived(objectMapper.readValue(expectedUpsertBPartnerRequestIS, BPUpsertCamelRequest.class));

		//validate JsonOLCandCreateBulkRequest
		final InputStream olCandCreateRequestIS = this.getClass().getResourceAsStream(JSON_OL_CAND_CREATE_REQUEST);

		final MockEndpoint olCandMockEndpoint = getMockEndpoint(MOCK_OL_CAND_CREATE);
		olCandMockEndpoint.expectedBodiesReceived(objectMapper.readValue(olCandCreateRequestIS, JsonOLCandCreateBulkRequest.class));

		//validate JsonOLCandClearRequest
		final InputStream jsonOLCandClearRequest = this.getClass().getResourceAsStream(JSON_OL_CAND_CLEAR_REQUEST);

		final MockEndpoint olCandClearEndpoint = getMockEndpoint(MOCK_OL_CAND_CLEAR);
		olCandClearEndpoint.expectedBodiesReceived(objectMapper.readValue(jsonOLCandClearRequest, JsonOLCandClearRequest.class));

		template.sendBody("direct:" + GET_ORDERS_ROUTE_ID, "Not relevant!");

		assertThat(createdBPartnerProcessor.called).isEqualTo(1);
		assertThat(successfullyCreatedOLCandProcessor.called).isEqualTo(1);
		assertThat(successfullyClearOrdersProcessor.called).isEqualTo(1);
		assertMockEndpointsSatisfied();
	}

	private void prepareRouteForTesting(
			final MockUpsertBPartnerProcessor upsertBPartnerProcessor,
			final MockSuccessfullyCreatedOLCandProcessor olCandProcessor,
			final MockSuccessfullyClearOrdersProcessor olCandClearProcessor) throws Exception
	{
		AdviceWith.adviceWith(context, GET_ORDERS_ROUTE_ID,
							  advice -> advice.weaveById(GET_ORDERS_PROCESSOR_ID)
									  .replace()
									  .process(new MockGetOrdersProcessor()));

		AdviceWith.adviceWith(context, PROCESS_ORDER_ROUTE_ID,
							  advice -> {
								  advice.weaveById(CREATE_BPARTNER_UPSERT_REQ_PROCESSOR_ID)
										  .after()
										  .to(MOCK_BPARTNER_UPSERT);

								  advice.interceptSendToEndpoint("{{" + ExternalSystemCamelConstants.MF_UPSERT_BPARTNER_V2_CAMEL_URI + "}}")
										  .skipSendToOriginalEndpoint()
										  .process(upsertBPartnerProcessor);

								  advice.weaveById(OLCAND_REQ_PROCESSOR_ID)
										  .after()
										  .to(MOCK_OL_CAND_CREATE);

								  advice.interceptSendToEndpoint("direct:" + ExternalSystemCamelConstants.MF_PUSH_OL_CANDIDATES_ROUTE_ID)
										  .skipSendToOriginalEndpoint()
										  .process(olCandProcessor);
							  });

		AdviceWith.adviceWith(context, CLEAR_ORDERS_ROUTE_ID,
							  advice -> advice.interceptSendToEndpoint("direct:" + ExternalSystemCamelConstants.MF_CLEAR_OL_CANDIDATES_ROUTE_ID)
									  .skipSendToOriginalEndpoint()
									  .to(MOCK_OL_CAND_CLEAR)
									  .process(olCandClearProcessor));
	}

	private static String loadAsString(@NonNull final String name)
	{
		final InputStream inputStream = GetOrdersRouteBuilderTests.class.getResourceAsStream(name);
		return new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8))
				.lines()
				.collect(Collectors.joining("\n"));
	}

	private static class MockSuccessfullyCreatedOLCandProcessor implements Processor
	{
		private int called = 0;

		@Override
		public void process(final Exchange exchange)
		{
			called++;
		}
	}

	private static class MockGetOrdersProcessor implements Processor
	{
		@Override
		public void process(final Exchange exchange) throws IOException
		{
			// mock getOrders
			final ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());

			final InputStream ordersIS = GetOrdersRouteBuilderTests.class.getResourceAsStream(JSON_ORDERS_RESOURCE_PATH);
			final JsonOrders jsonOrders = mapper.readValue(ordersIS, JsonOrders.class);

			final List<JsonOrderAndCustomId> jsonOrderAndCustomIds = jsonOrders
					.getData()
					.stream()
					.map(order -> JsonOrderAndCustomId.builder().jsonOrder(order).build())
					.collect(Collectors.toList());

			// mock shopware client
			final ShopwareClient shopwareClient = prepareShopwareClientMock(mapper);

			//set up the exchange
			final CurrencyInfoProvider currencyInfoProvider = CurrencyInfoProvider.builder()
					.currencyId2IsoCode(ImmutableMap.of(MOCK_CURRENCY_ID, MOCK_EUR_CODE))
					.build();

			final ImportOrdersRouteContext ordersContext = ImportOrdersRouteContext.builder()
					.orgCode(MOCK_ORG_CODE)
					.shopwareClient(shopwareClient)
					.currencyInfoProvider(currencyInfoProvider)
					.build();

			exchange.getIn().setBody(jsonOrderAndCustomIds);
			exchange.setProperty(ROUTE_PROPERTY_IMPORT_ORDERS_CONTEXT, ordersContext);

		}

		@NonNull
		private ShopwareClient prepareShopwareClientMock(final ObjectMapper mapper) throws IOException
		{
			final ShopwareClient dumbShopwareClient = ShopwareClient.of("does", "not", "https://www.matter.com");
			final ShopwareClient shopwareClientSpy = Mockito.spy(dumbShopwareClient);

			Mockito.doNothing().when(shopwareClientSpy).refreshTokenIfExpired();

			//1. mock getDeliveries
			final String deliveriesString = loadAsString(JSON_ORDER_DELIVERIES_PATH);

			Mockito.doReturn(ResponseEntity.ok(deliveriesString))
					.when(shopwareClientSpy)
					.performWithRetry(any(), eq(HttpMethod.GET), eq(String.class), any());

			//2. mock getOrderAddressDetails
			final InputStream billingAddressIS = GetOrdersRouteBuilderTests.class.getResourceAsStream(JSON_ORDER_BILLING_ADDRESS_PATH);
			final JsonOrderAddress billingAddress = mapper.readValue(billingAddressIS, JsonOrderAddress.class);

			Mockito.doReturn(Optional.of(JsonOrderAddressAndCustomId.builder()
												 .jsonOrderAddress(billingAddress)
												 .build()))
					.when(shopwareClientSpy)
					.getOrderAddressDetails(nullable(String.class), nullable(String.class));

			//3. mock getCountryDetails
			final InputStream countryIS = GetOrdersRouteBuilderTests.class.getResourceAsStream(JSON_COUNTRY_INFO_PATH);
			final JsonCountry jsonCountry = mapper.readValue(countryIS, JsonCountry.class);

			Mockito.doReturn(Optional.of(jsonCountry))
					.when(shopwareClientSpy)
					.getCountryDetails(any(String.class));

			//4. mock orderLines
			final InputStream orderLinesIS = GetOrdersRouteBuilderTests.class.getResourceAsStream(JSON_ORDER_LINES);
			final JsonOrderLines orderLines = mapper.readValue(orderLinesIS, JsonOrderLines.class);

			Mockito.doReturn(ResponseEntity.ok(orderLines))
					.when(shopwareClientSpy)
					.performWithRetry(any(), eq(HttpMethod.GET), eq(JsonOrderLines.class), any());

			return shopwareClientSpy;
		}
	}

	private static class MockUpsertBPartnerProcessor implements Processor
	{
		private int called = 0;

		@Override
		public void process(final Exchange exchange)
		{
			called++;
			final InputStream upsertBPartnerResponse = GetOrdersRouteBuilderTests.class.getResourceAsStream(JSON_UPSERT_BPARTNER_RESPONSE);
			exchange.getIn().setBody(upsertBPartnerResponse);
		}
	}

	private static class MockSuccessfullyClearOrdersProcessor implements Processor
	{
		private int called = 0;

		@Override
		public void process(final Exchange exchange)
		{
			called++;
		}
	}
}