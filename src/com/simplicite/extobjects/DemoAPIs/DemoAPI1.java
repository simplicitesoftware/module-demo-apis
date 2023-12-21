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

	private static final String PRODUCTS = "products";
	private static final String ORDERS = "orders";

	@Override
	public void init(Parameters params) {
		super.init(params);

		//addOperationDesc(null, OPERATION_PING, "This is the **ping** operation");

		//addOperationDesc(PRODUCTS, OPERATION_SEARCH, "This is the **search** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_GET, "This is the **get** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_CREATE, "This is the **create** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_UPDATE, "This is the **update** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_DELETE, "This is the **delete** operation for the _product_ business object");

		addObject(ORDERS, "DemoOrder", "Order");
		// or to force pagination (10 by 10): addObject(ORDERS, "DemoOrder", "Order", true, 10);
		addRefField(ORDERS, PRODUCTS, "productId", "demoOrdPrdId", "productOrders", getSettings().optBoolean("embedLinks"), "Reference to product's row ID");
		addField(ORDERS, "number", "demoOrdNumber");
		addField(ORDERS, "date", "demoOrdDate");
		addField(ORDERS, "status", "demoOrdStatus");
		addField(ORDERS, "productReference", "demoOrdPrdId.demoPrdReference");
		addField(ORDERS, "productName", "demoOrdPrdId.demoPrdName");
		addField(ORDERS, "productType", "demoOrdPrdId.demoPrdType");
		addField(ORDERS, "productSupplierCode", "demoOrdPrdId.demoPrdSupId.demoSupCode");
		addField(ORDERS, "productSupplierName", "demoOrdPrdId.demoPrdSupId.demoSupName");
	}
}
