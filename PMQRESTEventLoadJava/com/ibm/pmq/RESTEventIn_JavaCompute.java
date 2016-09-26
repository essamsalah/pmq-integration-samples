package com.ibm.pmq;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;
import com.ibm.broker.plugin.MbXMLNSC;

public class RESTEventIn_JavaCompute extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below
			// Check credentials
			boolean authorized = false;
			MbElement properties = inMessage.getRootElement().getFirstElementByPath("Properties");
			if (properties != null) {
				MbElement user = properties.getFirstElementByPath("IdentitySourceToken");
				MbElement password = properties.getFirstElementByPath("IdentitySourcePassword");
				if (user != null && password != null) {
					String userstr = user.getValueAsString();
					String passwordstr = password.getValueAsString();
					
					if ("******".equals(userstr) && "******".equals(passwordstr)) {
						authorized = true;
					}
				}
			}
			if (!authorized) {
				alt.propagate(outAssembly);
			}
			
			MbElement domainElement = inMessage.getRootElement().getFirstElementByPath("JSON");
			MbElement eventElement = domainElement.getFirstChild().getFirstChild().getFirstChild();

			MbElement outParser = outMessage.getRootElement().createElementAsLastChild(MbXMLNSC.PARSER_NAME);
			MbElement xmlEvent = outParser.createElementAsFirstChild(MbElement.TYPE_NAME, "event", null);
			
			// Assign values from the JSON event
			while (eventElement != null) {
				String name = eventElement.getName();
				Object value = eventElement.getValue();
				if (("observation".equals(name))){
					MbElement itemElement = eventElement.getFirstChild();
					while (itemElement != null) {
						MbElement observationElement = itemElement.getFirstChild();
						MbElement xmlObservation = xmlEvent.createElementAsLastChild(
								MbElement.TYPE_NAME_VALUE, "observation", null);
						while (observationElement != null) {
							xmlObservation.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, 
									observationElement.getName(), observationElement.getValue());
							observationElement = observationElement.getNextSibling();
						}
						itemElement = itemElement.getNextSibling();
					}
				} else{
					xmlEvent.createElementAsLastChild(MbElement.TYPE_NAME_VALUE, name, value);
				}
				eventElement = eventElement.getNextSibling();
			}
			
			// Remove HTTP and JSON from output
			outMessage.getRootElement().getFirstElementByPath("HTTPInputHeader").delete();
			outMessage.getRootElement().getFirstElementByPath("JSON").delete();


			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

}
