# iot-pmq-integration-samples (Update in progress)
Asset-intensive industries, such as oil and gas, mining, and energy and utilities, use complex equipment such as compressors, 
haul trucks, and turbines, in their day-to-day operation. Any unplanned downtime or major unforeseen failure of this equipment
has a direct impact on production downtime, which affects the financial performance of the organization. Potential component 
and equipment failure, plus machine health of in-service equipment needs to be monitored by identifying early signs of possible 
downtime. The goal is to maximize the uptime of the component/equipment.

The IBM Predictive Maintenance and Quality (PMQ) solution helps you monitor, analyze, and report on information that is gathered 
from high-value assets and recommend maintenance activities for them. With this integrated solution, you can:

* Predict the failure of a monitored asset in order to fix it and avoid costly downtime.
* Search stored maintenance logs to determine the best repair procedures and cycles.
* Identify the root causes of asset failure to take corrective actions.

The integration bus layer(IIB) within PMQ helps to transform external events (received from monitored high-value assets) into the format 
that is required by the PMQ analytics solution's data model. One way to receive external low-level events, such as the discharge 
pressure of a compressor or the inlet temperature of compressor, is to use the Watson IoT Platform.
