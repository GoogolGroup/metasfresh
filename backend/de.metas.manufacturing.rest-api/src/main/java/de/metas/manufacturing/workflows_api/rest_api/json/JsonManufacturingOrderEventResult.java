package de.metas.manufacturing.workflows_api.rest_api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.metas.manufacturing.workflows_api.activity_handlers.receive.json.JsonAggregateToExistingLU;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;
import java.math.BigDecimal;

@Data
@Jacksonized
public class JsonManufacturingOrderEventResult
{
	//
	// Activity Identifier
	@NonNull final String wfProcessId;
	@NonNull final String wfActivityId;

	//
	// Receive result
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Nullable JsonAggregateToExistingLU existingLU;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@Nullable BigDecimal qtyReceivedTotal;
}
