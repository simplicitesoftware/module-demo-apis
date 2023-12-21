package com.simplicite.extobjects.DemoAPIs;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.tools.JSONTool;
import com.simplicite.util.tools.Parameters;

/**
 * Custom mapped REST web services
 */
public class DemoAPI1 extends com.simplicite.webapp.services.RESTMappedObjectsExternalObject {
	private static final long serialVersionUID = 1L;

	private static final String SUPPLIERS = "suppliers";
	private static final String PRODUCTS = "products";
	private static final String ORDERS = "orders";
	private static final String STATS = "stats";

	@Override
	public void init(Parameters params) {
		JSONObject settings  = getSettings();

		boolean embedLinks = settings.optBoolean("embedLinks");

		setOpenAPISpec(settings.optInt("spec", JSONTool.OPENAPI_OAS3));
		if (settings.has("desc"))
			setOpenAPIDesc(settings.getString("desc"));
		if (settings.has("version"))
			setOpenAPIVers(settings.getString("version"));

		//addOperationDesc(null, OPERATION_PING, "This is the **ping** operation");

		/*JSONArray objects = settings.optJSONArray("objects");
		if (objects != null) {
			for (int i = 0; i < objects.length(); i++) {
				JSONObject object = objects.getJSONObject(i);
				String objectName = object.getString("name");
				addObject(objectName, object.getString("object"), object.optString("desc"));
				JSONArray fields = object.optJSONArray("fields");
				if (fields != null) {
					for (int j = 0; j < fields.length(); j++) {
						JSONObject field = fields.getJSONObject(j);
						addField(objectName, field.getString("name"), field.getString("field"));
					}
				}
			} 
		}*/

		addObject(SUPPLIERS, "DemoSupplier", "Supplier");
		addField(SUPPLIERS, "code", "demoSupCode");
		addField(SUPPLIERS, "name", "demoSupName");

		addObject(PRODUCTS, "DemoProduct", "Product");
		addRefField(PRODUCTS, SUPPLIERS, "supplierId", "demoPrdSupId", "supplierProducts", embedLinks, "Reference to supplier's row ID");
		addField(PRODUCTS, "supplierCode", "demoPrdSupId.demoSupCode", "Supplier code", null);
		addField(PRODUCTS, "supplierName", "demoPrdSupId.demoSupName", "Supplier name", null);
		addField(PRODUCTS, "reference", "demoPrdReference", "Product reference", "REFxxx");
		addField(PRODUCTS, "type", "demoPrdType", "Product type", null);
		addField(PRODUCTS, "name", "demoPrdName", "Product name", null);
		addField(PRODUCTS, "available", "demoPrdAvailable", "Available product?", null);

		addObject(ORDERS, "DemoOrder", "Order");
		// or to force pagination (10 by 10): addObject(ORDERS, "DemoOrder", "Order", true, 10);
		addRefField(ORDERS, PRODUCTS, "productId", "demoOrdPrdId", "productOrders", embedLinks, "Reference to product's row ID");
		addField(ORDERS, "number", "demoOrdNumber");
		addField(ORDERS, "date", "demoOrdDate");
		addField(ORDERS, "status", "demoOrdStatus");
		addField(ORDERS, "productReference", "demoOrdPrdId.demoPrdReference");
		addField(ORDERS, "productName", "demoOrdPrdId.demoPrdName");
		addField(ORDERS, "productType", "demoOrdPrdId.demoPrdType");
		addField(ORDERS, "productSupplierCode", "demoOrdPrdId.demoPrdSupId.demoSupCode");
		addField(ORDERS, "productSupplierName", "demoOrdPrdId.demoPrdSupId.demoSupName");
		//addOperationDesc(PRODUCTS, OPERATION_SEARCH, "This is the **search** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_GET, "This is the **get** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_CREATE, "This is the **create** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_UPDATE, "This is the **update** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_DELETE, "This is the **delete** operation for the _product_ business object");

		addObject(STATS, "DemoStats", DESC_HIDDEN_FROM_SCHEMA);
		addField(STATS, "status", "demoOrdStatus");
		addField(STATS, "count", "demoStsCount");
		addField(STATS, "quantity", "demoStsQuantity");
		addField(STATS, "total", "demoStsTotal");
	}
}
