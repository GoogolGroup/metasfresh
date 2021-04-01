
-- 2021-04-01T08:48:12.494Z
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_Column (AD_Reference_ID,FieldLength,Version,IsKey,IsParent,IsTranslated,IsIdentifier,SeqNo,AD_Client_ID,IsActive,Created,CreatedBy,IsUpdateable,DDL_NoForeignKey,IsSelectionColumn,IsSyncDatabase,IsAlwaysUpdateable,IsAutocomplete,IsAllowLogging,IsEncrypted,Updated,UpdatedBy,IsAdvancedText,IsLazyLoading,AD_Table_ID,IsCalculated,AD_Column_ID,IsDimension,IsMandatory,IsStaleable,IsUseDocSequence,IsShowFilterIncrementButtons,IsDLMPartitionBoundary,IsGenericZoomKeyColumn,SelectionColumnSeqNo,EntityType,IsForceIncludeInGeneratedModel,IsGenericZoomOrigin,ColumnName,IsAutoApplyValidationRule,Description,IsFacetFilter,MaxFacetsToFetch,FacetFilterSeqNo,AD_Element_ID,IsShowFilterInline,Name,AD_Org_ID) VALUES (10,255,0,'N','N','N','N',0,0,'Y',TO_TIMESTAMP('2021-04-01 10:48:12','YYYY-MM-DD HH24:MI:SS'),100,'N','N','N','N','N','N','Y','N',TO_TIMESTAMP('2021-04-01 10:48:12','YYYY-MM-DD HH24:MI:SS'),100,'N','N',540466,'N',573327,'N','N','N','N','N','N','N',0,'de.metas.esb.edi','N','N','Setup_Place_No','N','','N',0,0,578643,'N','Setup Place No.',0)
;

-- 2021-04-01T08:48:12.497Z
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_Column_Trl (AD_Language,AD_Column_ID, Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language, t.AD_Column_ID, t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Column t WHERE l.IsActive='Y'AND (l.IsSystemLanguage='Y') AND t.AD_Column_ID=573327 AND NOT EXISTS (SELECT 1 FROM AD_Column_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Column_ID=t.AD_Column_ID)
;

-- 2021-04-01T08:48:12.499Z
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
/* DDL */  select update_Column_Translation_From_AD_Element(578643)
;

-- 2021-04-01T08:48:49.868
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO EXP_FormatLine (AD_Client_ID,AD_Column_ID,AD_Org_ID,Created,CreatedBy,EntityType,EXP_Format_ID,EXP_FormatLine_ID,IsActive,IsMandatory,IsPartUniqueIndex,Name,Position,Type,Updated,UpdatedBy,Value) VALUES (0,573327,0,TO_TIMESTAMP('2021-04-01 10:48:49','YYYY-MM-DD HH24:MI:SS'),100,'de.metas.esb.edi',540224,550322,'Y','N','N','Setup_Place_No',200,'E',TO_TIMESTAMP('2021-04-01 10:48:49','YYYY-MM-DD HH24:MI:SS'),100,'Setup_Place_No')
;

-- View: EDI_Cctop_119_v

DROP VIEW IF EXISTS EDI_Cctop_119_v;
CREATE OR REPLACE VIEW EDI_Cctop_119_v AS
SELECT lookup.C_Invoice_ID       AS EDI_Cctop_119_v_ID,
       lookup.C_Invoice_ID,
       lookup.C_Invoice_ID       AS EDI_Cctop_INVOIC_v_ID,
       lookup.M_InOut_ID,
       pl.C_BPartner_Location_ID,
       pl.GLN,
       pl.Phone,
       pl.Fax,
       p.Name,
       p.Name2,
       p.Value,
       p.VATaxID,
       lookup.Vendor_ReferenceNo AS ReferenceNo,
       CASE lookup.Type_V
           WHEN 'ship'::TEXT THEN 'DP'::TEXT
           WHEN 'bill'::TEXT THEN 'IV'::TEXT
           WHEN 'cust'::TEXT THEN 'BY'::TEXT
           WHEN 'vend'::TEXT THEN 'SU'::TEXT
           WHEN 'snum'::TEXT THEN 'SN'::TEXT
                             ELSE 'Error EANCOM_Type'::TEXT
       END                       AS eancom_locationtype,
       l.Address1,
       l.Address2,
       l.Postal,
       l.City,
       c.CountryCode,
       l.AD_Client_ID,
       l.AD_Org_ID,
       l.Created,
       l.CreatedBy,
       l.Updated,
       l.UpdatedBy,
       l.IsActive
FROM (
         SELECT union_lookup.*
         FROM (
                  SELECT DISTINCT 1::INTEGER   AS SeqNo
                                , 'cust'::TEXT AS Type_V
                                , pl_cust.C_BPartner_Location_ID
                                , i.C_Invoice_ID
                                , 0::INTEGER   AS M_InOut_ID
                                , NULL::TEXT   AS Vendor_ReferenceNo
                                , pl_cust.Setup_Place_No
                  FROM C_Invoice i
                           LEFT JOIN C_Invoiceline il ON il.C_Invoice_ID = i.C_Invoice_ID
                           LEFT JOIN C_OrderLine ol ON ol.C_OrderLine_ID = il.C_OrderLine_ID
                           LEFT JOIN C_Order o ON o.C_Order_ID = ol.C_Order_ID
                           LEFT JOIN C_BPartner_Location pl_cust ON pl_cust.C_BPartner_Location_ID =
                                                                    (CASE
                                                                         WHEN o.C_BPartner_Location_ID IS NOT NULL AND o.C_BPartner_Location_ID != 0::INTEGER
                                                                             THEN o.C_BPartner_Location_ID
                                                                             ELSE -- Fallback if the C_Invoice is Hand Solo :) (I mean no C_Order)
                                                                             i.C_BPartner_Location_ID
                                                                     END)
                       --
                  UNION
                  --
                  SELECT 2::INTEGER         AS SeqNo
                       , 'vend'::TEXT       AS Type_V
                       , pl_vend.C_BPartner_Location_ID
                       , i.C_Invoice_ID
                       , 0::INTEGER         AS M_InOut_ID
                       , p_cust.ReferenceNo AS Vendor_ReferenceNo
                       , pl_vend.Setup_Place_No
                  FROM C_Invoice i
                           JOIN C_BPartner p_cust ON p_cust.C_BPartner_ID = i.C_BPartner_ID
                           JOIN C_BPartner p_vend ON p_vend.AD_OrgBP_ID = i.AD_Org_ID
                           JOIN C_BPartner_Location pl_vend ON pl_vend.C_BPartner_ID = p_vend.C_BPartner_ID AND pl_vend.isremitto = 'Y'
                       --
                  UNION
                  --
                  SELECT DISTINCT 3::INTEGER   AS SeqNo
                                , 'ship'::TEXT AS Type_V
                                , pl_ship.c_bpartner_location_id
                                , i.C_Invoice_ID
                                , 0::INTEGER   AS M_InOut_ID
                                , NULL::TEXT   AS Vendor_ReferenceNo
                                , pl_ship.Setup_Place_No
                  FROM C_Invoice i
                           INNER JOIN C_Invoiceline il ON il.C_Invoice_ID = i.C_Invoice_ID
                           LEFT JOIN C_OrderLine ol ON ol.C_OrderLine_ID = il.C_OrderLine_ID
                           LEFT JOIN C_Order o ON o.C_Order_ID = ol.C_Order_ID
                           LEFT JOIN M_InOutline sl ON ( -- try to join directly from C_InvoiceLine
                                                                   sl.M_InOutLine_ID = il.M_InOutLine_ID AND il.M_InOutLine_ID IS NOT NULL AND il.M_InOutLine_ID != 0)
                      -- fallback and try to join from C_OrderLine if (and only if) it's missing in C_InvoiceLine
                      OR (sl.C_OrderLine_ID = il.C_OrderLine_ID AND (il.M_InOutLine_ID IS NULL OR il.M_InOutLine_ID = 0))
                           LEFT JOIN M_InOut s ON s.M_InOut_ID = sl.M_InOut_ID

                           LEFT JOIN C_BPartner_Location pl_ship ON pl_ship.C_BPartner_Location_ID =
                                                                    (CASE -- Could use COALESCE(NULLIF(.., ..), ..) here, but it gets messy, so I prefer CASE
                                                                         WHEN o.HandOver_Location_ID IS NOT NULL AND o.HandOver_Location_ID != 0::INTEGER
                                                                             THEN o.HandOver_Location_ID
                                                                         WHEN s.C_BPartner_Location_ID IS NOT NULL AND s.C_BPartner_Location_ID != 0::INTEGER
                                                                             THEN s.C_BPartner_Location_ID
                                                                         WHEN o.DropShip_Location_ID IS NOT NULL AND o.DropShip_Location_ID != 0::INTEGER
                                                                             THEN o.DropShip_Location_ID
                                                                         WHEN o.C_BPartner_Location_ID IS NOT NULL AND o.C_BPartner_Location_ID != 0::INTEGER
                                                                             THEN o.C_BPartner_Location_ID
                                                                             ELSE -- Fallback if the C_Invoice is Hand Solo :) (I mean no C_Order)
                                                                             i.C_BPartner_Location_ID
                                                                     END)
                       --
                  UNION
                  --
                  SELECT 4::INTEGER   AS SeqNo
                       , 'bill'::TEXT AS Type_V
                       , pl_bill.C_BPartner_Location_ID
                       , i.C_Invoice_ID
                       , 0::INTEGER   AS M_InOut_ID
                       , NULL::TEXT   AS Vendor_ReferenceNo
                       , pl_bill.Setup_Place_No
                  FROM C_Invoice i
                           LEFT JOIN C_BPartner_Location pl_bill ON pl_bill.C_BPartner_Location_ID = i.C_BPartner_Location_ID
                       --
                  UNION
                  --
                  SELECT DISTINCT 5::INTEGER   AS SeqNo
                                , 'snum'::TEXT AS Type_V
                                , pl_snum.C_BPartner_Location_ID
                                , i.C_Invoice_ID
                                , s.M_InOut_ID
                                , NULL::TEXT   AS Vendor_ReferenceNo
                                , pl_snum.Setup_Place_No
                  FROM C_Invoice i
                           INNER JOIN C_Invoiceline il ON il.C_Invoice_ID = i.C_Invoice_ID
                           LEFT JOIN C_OrderLine ol ON ol.C_OrderLine_ID = il.C_OrderLine_ID
                           LEFT JOIN C_Order o ON o.C_Order_ID = ol.C_Order_ID
                           LEFT JOIN M_InOutline sl ON ( -- try to join directly from C_InvoiceLine
                                                                   sl.M_InOutLine_ID = il.M_InOutLine_ID AND il.M_InOutLine_ID IS NOT NULL AND il.M_InOutLine_ID != 0)
                      -- fallback and try to join from C_OrderLine if (and only if) it's missing in C_InvoiceLine
                      OR (sl.C_OrderLine_ID = il.C_OrderLine_ID AND (il.M_InOutLine_ID IS NULL OR il.M_InOutLine_ID = 0))
                           LEFT JOIN M_InOut s ON s.M_InOut_ID = sl.M_InOut_ID
                           LEFT JOIN C_BPartner_Location pl_snum ON pl_snum.C_BPartner_Location_ID =
                                                                    (CASE -- Could use COALESCE(NULLIF(.., ..), ..) here, but it gets messy, so I prefer CASE
                                                                         WHEN s.C_BPartner_Location_ID IS NOT NULL AND s.C_BPartner_Location_ID != 0::INTEGER
                                                                             THEN s.C_BPartner_Location_ID
                                                                         WHEN o.DropShip_Location_ID IS NOT NULL AND o.DropShip_Location_ID != 0::INTEGER
                                                                             THEN o.DropShip_Location_ID
                                                                         WHEN o.C_BPartner_Location_ID IS NOT NULL AND o.C_BPartner_Location_ID != 0::INTEGER
                                                                             THEN o.C_BPartner_Location_ID
                                                                             ELSE -- Fallback if the C_Invoice is Hand Solo :) (I mean no C_Order)
                                                                             i.C_BPartner_Location_ID
                                                                     END)
              ) union_lookup
         ORDER BY union_lookup.SeqNo, union_lookup.Type_V, union_lookup.C_BPartner_Location_ID, C_Invoice_ID, M_InOut_ID
     ) lookup
         LEFT JOIN C_BPartner_Location pl ON pl.C_BPartner_Location_ID = lookup.C_BPartner_Location_ID
         LEFT JOIN C_BPartner p ON p.C_BPartner_ID = pl.C_BPartner_ID
         LEFT JOIN C_Location l ON l.C_Location_ID = pl.C_Location_ID
         LEFT JOIN C_Country c ON c.C_Country_ID = l.C_Country_ID
WHERE TRUE
  AND p.VATaxID IS NOT NULL
  AND (l.Address1 IS NOT NULL OR l.Address2 IS NOT NULL)
ORDER BY (
          lookup.C_Invoice_ID,
          CASE lookup.Type_V
              WHEN 'ship'::TEXT THEN 'DP'::TEXT
              WHEN 'bill'::TEXT THEN 'IV'::TEXT
              WHEN 'cust'::TEXT THEN 'BY'::TEXT
              WHEN 'vend'::TEXT THEN 'SU'::TEXT
              WHEN 'snum'::TEXT THEN 'SN'::TEXT
                                ELSE 'Error EANCOM_Type'::TEXT
          END)
;