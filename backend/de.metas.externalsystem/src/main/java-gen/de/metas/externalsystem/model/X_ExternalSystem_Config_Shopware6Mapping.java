// Generated Model - DO NOT CHANGE
package de.metas.externalsystem.model;

import java.sql.ResultSet;
import java.util.Properties;
import javax.annotation.Nullable;

/** Generated Model for ExternalSystem_Config_Shopware6Mapping
 *  @author metasfresh (generated) 
 */
@SuppressWarnings("unused")
public class X_ExternalSystem_Config_Shopware6Mapping extends org.compiere.model.PO implements I_ExternalSystem_Config_Shopware6Mapping, org.compiere.model.I_Persistent 
{

	private static final long serialVersionUID = 1256899093L;

    /** Standard Constructor */
    public X_ExternalSystem_Config_Shopware6Mapping (final Properties ctx, final int ExternalSystem_Config_Shopware6Mapping_ID, @Nullable final String trxName)
    {
      super (ctx, ExternalSystem_Config_Shopware6Mapping_ID, trxName);
    }

    /** Load Constructor */
    public X_ExternalSystem_Config_Shopware6Mapping (final Properties ctx, final ResultSet rs, @Nullable final String trxName)
    {
      super (ctx, rs, trxName);
    }


	/** Load Meta Data */
	@Override
	protected org.compiere.model.POInfo initPO(final Properties ctx)
	{
		return org.compiere.model.POInfo.getPOInfo(Table_Name);
	}

	@Override
	public void setC_DocTypeOrder_ID (final int C_DocTypeOrder_ID)
	{
		if (C_DocTypeOrder_ID < 1) 
			set_Value (COLUMNNAME_C_DocTypeOrder_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocTypeOrder_ID, C_DocTypeOrder_ID);
	}

	@Override
	public int getC_DocTypeOrder_ID() 
	{
		return get_ValueAsInt(COLUMNNAME_C_DocTypeOrder_ID);
	}

	@Override
	public void setC_PaymentTerm_ID (final int C_PaymentTerm_ID)
	{
		if (C_PaymentTerm_ID < 1) 
			set_Value (COLUMNNAME_C_PaymentTerm_ID, null);
		else 
			set_Value (COLUMNNAME_C_PaymentTerm_ID, C_PaymentTerm_ID);
	}

	@Override
	public int getC_PaymentTerm_ID() 
	{
		return get_ValueAsInt(COLUMNNAME_C_PaymentTerm_ID);
	}

	@Override
	public void setDescription (final @Nullable java.lang.String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	@Override
	public java.lang.String getDescription() 
	{
		return get_ValueAsString(COLUMNNAME_Description);
	}

	@Override
	public de.metas.externalsystem.model.I_ExternalSystem_Config_Shopware6 getExternalSystem_Config_Shopware6()
	{
		return get_ValueAsPO(COLUMNNAME_ExternalSystem_Config_Shopware6_ID, de.metas.externalsystem.model.I_ExternalSystem_Config_Shopware6.class);
	}

	@Override
	public void setExternalSystem_Config_Shopware6(final de.metas.externalsystem.model.I_ExternalSystem_Config_Shopware6 ExternalSystem_Config_Shopware6)
	{
		set_ValueFromPO(COLUMNNAME_ExternalSystem_Config_Shopware6_ID, de.metas.externalsystem.model.I_ExternalSystem_Config_Shopware6.class, ExternalSystem_Config_Shopware6);
	}

	@Override
	public void setExternalSystem_Config_Shopware6_ID (final int ExternalSystem_Config_Shopware6_ID)
	{
		if (ExternalSystem_Config_Shopware6_ID < 1) 
			set_Value (COLUMNNAME_ExternalSystem_Config_Shopware6_ID, null);
		else 
			set_Value (COLUMNNAME_ExternalSystem_Config_Shopware6_ID, ExternalSystem_Config_Shopware6_ID);
	}

	@Override
	public int getExternalSystem_Config_Shopware6_ID() 
	{
		return get_ValueAsInt(COLUMNNAME_ExternalSystem_Config_Shopware6_ID);
	}

	@Override
	public void setExternalSystem_Config_Shopware6Mapping_ID (final int ExternalSystem_Config_Shopware6Mapping_ID)
	{
		if (ExternalSystem_Config_Shopware6Mapping_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_ExternalSystem_Config_Shopware6Mapping_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_ExternalSystem_Config_Shopware6Mapping_ID, ExternalSystem_Config_Shopware6Mapping_ID);
	}

	@Override
	public int getExternalSystem_Config_Shopware6Mapping_ID() 
	{
		return get_ValueAsInt(COLUMNNAME_ExternalSystem_Config_Shopware6Mapping_ID);
	}

	/** 
	 * PaymentRule AD_Reference_ID=195
	 * Reference name: _Payment Rule
	 */
	public static final int PAYMENTRULE_AD_Reference_ID=195;
	/** Cash = B */
	public static final String PAYMENTRULE_Cash = "B";
	/** CreditCard = K */
	public static final String PAYMENTRULE_CreditCard = "K";
	/** DirectDeposit = T */
	public static final String PAYMENTRULE_DirectDeposit = "T";
	/** Check = S */
	public static final String PAYMENTRULE_Check = "S";
	/** OnCredit = P */
	public static final String PAYMENTRULE_OnCredit = "P";
	/** DirectDebit = D */
	public static final String PAYMENTRULE_DirectDebit = "D";
	/** Mixed = M */
	public static final String PAYMENTRULE_Mixed = "M";
	/** PayPal = L */
	public static final String PAYMENTRULE_PayPal = "L";
	@Override
	public void setPaymentRule (final @Nullable java.lang.String PaymentRule)
	{
		set_Value (COLUMNNAME_PaymentRule, PaymentRule);
	}

	@Override
	public java.lang.String getPaymentRule() 
	{
		return get_ValueAsString(COLUMNNAME_PaymentRule);
	}

	@Override
	public void setSeqNo (final int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, SeqNo);
	}

	@Override
	public int getSeqNo() 
	{
		return get_ValueAsInt(COLUMNNAME_SeqNo);
	}

	@Override
	public void setSW6_Customer_Group (final @Nullable java.lang.String SW6_Customer_Group)
	{
		set_Value (COLUMNNAME_SW6_Customer_Group, SW6_Customer_Group);
	}

	@Override
	public java.lang.String getSW6_Customer_Group() 
	{
		return get_ValueAsString(COLUMNNAME_SW6_Customer_Group);
	}

	/** 
	 * SW6_Payment_Method AD_Reference_ID=541295
	 * Reference name: _SW6_Payment_Method
	 */
	public static final int SW6_PAYMENT_METHOD_AD_Reference_ID=541295;
	/** Nachnahme = cash_payment */
	public static final String SW6_PAYMENT_METHOD_Nachnahme = "cash_payment";
	/** Vorkasse = pre_payment */
	public static final String SW6_PAYMENT_METHOD_Vorkasse = "pre_payment";
	/** Rechnung = invoice_payment */
	public static final String SW6_PAYMENT_METHOD_Rechnung = "invoice_payment";
	/** SEPA = debit_payment */
	public static final String SW6_PAYMENT_METHOD_SEPA = "debit_payment";
	/** PayPal = pay_pal_payment_handler */
	public static final String SW6_PAYMENT_METHOD_PayPal = "pay_pal_payment_handler";
	/** Rechnungskauf Paypal = pay_pal_pui_payment_handler */
	public static final String SW6_PAYMENT_METHOD_RechnungskaufPaypal = "pay_pal_pui_payment_handler";
	@Override
	public void setSW6_Payment_Method (final @Nullable java.lang.String SW6_Payment_Method)
	{
		set_Value (COLUMNNAME_SW6_Payment_Method, SW6_Payment_Method);
	}

	@Override
	public java.lang.String getSW6_Payment_Method() 
	{
		return get_ValueAsString(COLUMNNAME_SW6_Payment_Method);
	}
}