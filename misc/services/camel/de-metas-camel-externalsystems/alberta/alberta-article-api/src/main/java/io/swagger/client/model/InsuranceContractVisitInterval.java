/*
 * #%L
 * alberta-article-api
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

/*
 * Artikel - Warenwirtschaft (Basis)
 * Synchronisation der Artikel mit Kumavision
 *
 * OpenAPI spec version: 1.0.2
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Objects;
/**
 * Besuchsintervall
 */
@Schema(description = "Besuchsintervall")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-03-11T10:09:42.333Z[GMT]")
public class InsuranceContractVisitInterval {
  @SerializedName("frequency")
  private BigDecimal frequency = null;

  @SerializedName("amount")
  private BigDecimal amount = null;

  @SerializedName("timePeriod")
  private BigDecimal timePeriod = null;

  @SerializedName("annotation")
  private String annotation = null;

  public InsuranceContractVisitInterval frequency(BigDecimal frequency) {
    this.frequency = frequency;
    return this;
  }

   /**
   * Häufigkeit - x Mal
   * @return frequency
  **/
  @Schema(example = "2", description = "Häufigkeit - x Mal")
  public BigDecimal getFrequency() {
    return frequency;
  }

  public void setFrequency(BigDecimal frequency) {
    this.frequency = frequency;
  }

  public InsuranceContractVisitInterval amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Anzahl der Tage, Wochen, Monate
   * @return amount
  **/
  @Schema(example = "1", description = "Anzahl der Tage, Wochen, Monate")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public InsuranceContractVisitInterval timePeriod(BigDecimal timePeriod) {
    this.timePeriod = timePeriod;
    return this;
  }

   /**
   * Zeitintervall (Unbekannt &#x3D; 0, Minute &#x3D; 1, Stunde &#x3D; 2, Tag &#x3D; 3, Woche &#x3D; 4, Monat &#x3D; 5, Quartal &#x3D; 6, Halbjahr &#x3D; 7, Jahr &#x3D; 8)
   * @return timePeriod
  **/
  @Schema(example = "6", description = "Zeitintervall (Unbekannt = 0, Minute = 1, Stunde = 2, Tag = 3, Woche = 4, Monat = 5, Quartal = 6, Halbjahr = 7, Jahr = 8)")
  public BigDecimal getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(BigDecimal timePeriod) {
    this.timePeriod = timePeriod;
  }

  public InsuranceContractVisitInterval annotation(String annotation) {
    this.annotation = annotation;
    return this;
  }

   /**
   * Bemerkung zum Besuchsintervall
   * @return annotation
  **/
  @Schema(example = "nur bei der Erstversorgung", description = "Bemerkung zum Besuchsintervall")
  public String getAnnotation() {
    return annotation;
  }

  public void setAnnotation(String annotation) {
    this.annotation = annotation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsuranceContractVisitInterval insuranceContractVisitInterval = (InsuranceContractVisitInterval) o;
    return Objects.equals(this.frequency, insuranceContractVisitInterval.frequency) &&
        Objects.equals(this.amount, insuranceContractVisitInterval.amount) &&
        Objects.equals(this.timePeriod, insuranceContractVisitInterval.timePeriod) &&
        Objects.equals(this.annotation, insuranceContractVisitInterval.annotation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(frequency, amount, timePeriod, annotation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsuranceContractVisitInterval {\n");
    
    sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    timePeriod: ").append(toIndentedString(timePeriod)).append("\n");
    sb.append("    annotation: ").append(toIndentedString(annotation)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}