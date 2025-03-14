package com.simplicite.extobjects.DemoAPIs;

import com.simplicite.util.tools.JSONTool;
import com.simplicite.util.tools.Parameters;

/**
 * Custom mapped REST web services
 */
public class DemoAPI1 extends com.simplicite.webapp.services.RESTMappedObjectsExternalObject {
	private static final long serialVersionUID = 1L;

	// Embbed linked lists?
	private static final boolean EMBED_LINKS = true;

	private static final String SUPPLIERS = "suppliers";
	private static final String PRODUCTS = "products";
	private static final String ORDERS = "orders";

	@Override
	public void init(Parameters params) {
		setOpenAPISpec(JSONTool.OPENAPI_OAS2);
		setOpenAPIDesc("This is a **simplified** variant of the demo API for the following business objects:\n\n- Suppliers\n- Products\n- Orders");
		setOpenAPIVers("v1");

		addObject(SUPPLIERS, "DemoSupplier", "Supplier");
		addField(SUPPLIERS, "code", "demoSupCode");
		addField(SUPPLIERS, "name", "demoSupName");

		addObject(PRODUCTS, "DemoProduct", "Product");
		addRefField(PRODUCTS, SUPPLIERS, "supplierId", "demoPrdSupId", "supplierProducts", EMBED_LINKS, "Reference to supplier's row ID");
		addField(PRODUCTS, "supplierCode", "demoPrdSupId.demoSupCode", "Supplier code", null);
		addField(PRODUCTS, "supplierName", "demoPrdSupId.demoSupName", "Supplier name", null);
		addField(PRODUCTS, "reference", "demoPrdReference", "Product reference", "REFxxx");
		addField(PRODUCTS, "type", "demoPrdType", "Product type", null);
		addField(PRODUCTS, "name", "demoPrdName", "Product name", null);
		addField(PRODUCTS, "available", "demoPrdAvailable", "Available product?", null);
		//addOperationDesc(PRODUCTS, OPERATION_SEARCH, "This is the **search** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_GET, "This is the **get** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_CREATE, "This is the **create** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_UPDATE, "This is the **update** operation for the _product_ business object");
		//addOperationDesc(PRODUCTS, OPERATION_DELETE, "This is the **delete** operation for the _product_ business object");

		if (getGrant().accessObject("DemoOrder")) {
			addObject(ORDERS, "DemoOrder", "Order");
			// or to force pagination (10 by 10): addObject(ORDERS, "DemoOrder", "Order", true, 10);
			addRefField(ORDERS, PRODUCTS, "productId", "demoOrdPrdId", "productOrders", EMBED_LINKS, "Reference to product's row ID");
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
}
