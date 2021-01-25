package jc.tools.pub;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class math

{
	// ---( internal utility methods )---

	final static math _instance = new math();

	static math _newInstance() { return new math(); }

	static math _cast(Object o) { return (math)o; }

	// ---( server methods )---




	public static final void calculateRepaymentAmount (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(calculateRepaymentAmount)>> ---
		// @sigtype java 3.5
		// [i] field:0:required loanAmount
		// [i] field:0:required termInYears
		// [i] field:0:optional termInMonths
		// [i] field:0:required interestRate
		// [o] field:0:required monthlyRate
		// [o] field:0:required creditCost
		// [o] field:0:required totalAmount
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String	loanAmount = IDataUtil.getString(pipelineCursor, "loanAmount");
		String	termInYears = IDataUtil.getString(pipelineCursor, "termInYears");
		String	termInMonths = IDataUtil.getString(pipelineCursor, "termInMonths");
		String	interestRate = IDataUtil.getString(pipelineCursor, "interestRate");
		
		// process
		
		if (termInMonths == null)
		      termInMonths = "" + Integer.parseInt(termInYears) * 12;
		
		double monthlyRate = calculateMonthlyPayment(Integer.parseInt(loanAmount), Integer.parseInt(termInMonths), Double.parseDouble(interestRate));
		double creditCost = (monthlyRate * Integer.parseInt(termInMonths)) - Integer.parseInt(loanAmount);
		
		// pipeline out
		
		IDataUtil.put(pipelineCursor, "monthlyRate","" + monthlyRate);
		IDataUtil.put(pipelineCursor, "creditCost", "" + creditCost);
		IDataUtil.put(pipelineCursor, "totalAmount", "" + creditCost + Integer.parseInt(loanAmount));
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void divideInts (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(divideInts)>> ---
		// @sigtype java 3.5
		// [i] field:0:required num1
		// [i] field:0:required num2
		// [o] field:0:required value
		// pipeline in
		
		IDataCursor pipelineCursor = pipeline.getCursor();
		String num1 = IDataUtil.getString(pipelineCursor, "num1");
		String num2 = IDataUtil.getString(pipelineCursor, "num2");
		
		// process out
		 
		int numi1 = Integer.parseInt(num1);
		int numi2 = Integer.parseInt(num2);
		
		int value = (int) Math.ceil(((float) numi1) / numi2);
		
		// pipeline out
			
		IDataUtil.put(pipelineCursor, "value", "" + value);
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	public static double calculateMonthlyPayment(int loanAmount, int termInMonths, double interestRate) {
		       
		interestRate /= 100.0;     
		double monthlyRate = interestRate / 12.0;
		double monthlyPayment = 
		     
		(loanAmount*monthlyRate) / 
		    (1-Math.pow(1+monthlyRate, -termInMonths));
		       
		      return monthlyPayment;
	}
	// --- <<IS-END-SHARED>> ---
}

