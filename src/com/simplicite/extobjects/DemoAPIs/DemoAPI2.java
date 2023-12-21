package com.simplicite.extobjects.DemoAPIs;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.ObjectDB;
import com.simplicite.util.annotations.RESTService;
import com.simplicite.util.annotations.RESTServiceParam;
import com.simplicite.util.annotations.RESTServiceOperation;
import com.simplicite.util.exceptions.HTTPException;
import com.simplicite.util.exceptions.SearchException;
import com.simplicite.util.tools.Parameters;
import com.simplicite.util.tools.JSONTool;

/**
 * Custom REST web services (GET = search/select only)
 */
@RESTService(title = "Custom REST API example", desc = "Custom REST API example for the demo application")
public class DemoAPI2 extends com.simplicite.webapp.services.RESTServiceExternalObject {
	private static final long serialVersionUID = 1L;

	@RESTServiceOperation(method = "get", path = "/{object}", desc = "Search for specified object")
	public JSONArray searchObjectRecords(
			@RESTServiceParam(name = "object", in = "path", desc = "Object name")
			String object
		) throws SearchException {
		return searchObjectRecords(object, null);
	}

	@RESTServiceOperation(method = "post", path = "/{object}", desc = "Search for specified object with filters")
	public JSONArray searchObjectRecords(
			@RESTServiceParam(name = "object", in = "path", desc = "Object name")
			String object,
			@RESTServiceParam(name = "filters", in = "body", type="object", desc = "Search filters")
			JSONObject filters
		) throws SearchException {
		ObjectDB obj = null;
		try {
			obj = borrowAPIObject(object); // Borrow an API object instance from the pool (ZZZ must be returned, see below)

			List<String[]> rows = obj.getTool().search(filters); // filtered search

			// Remove standard row IDs
			JSONArray list = new JSONArray(obj.toJSON(rows, null, false, true, false));
			for (int i = 0; i < list.length(); i++)
				list.getJSONObject(i).remove("row_id");

			return list;
		} finally {
			returnAPIObject(obj); // Return the API object intance to the pool
		}
	}
	
	@RESTServiceOperation(method = "get", path = "/{object}/{value}", desc = "Get for specified object")
	public JSONObject getObjectRecord(
			@RESTServiceParam(name = "object", in = "path", desc = "Object name")
			String object,
			@RESTServiceParam(name = "value", in = "path", desc = "ID field value, must denote a unique record")
			String value
		) throws SearchException {
		ObjectDB obj = null;
		try {
			obj = borrowAPIObject(object); // Borrow an API object instance from the pool (ZZZ must be returned, see below)

			String field = getSettings().getString(object);
			List<String[]> rows = obj.getTool().search(new JSONObject().put(field, value)); // filtered search using reference only

			// No unique record matching the custom ID field's value => not found
			if (rows.size() != 1)
				return notFound("No " + object + " for " + field + " = " + value);

			// Remove standard row ID
			JSONObject item = new JSONObject(obj.toJSON(rows.get(0), null, false, true));
			item.remove("row_id");

			return item;
		} finally {
			returnAPIObject(obj); // Return the API object intance to the pool
		}
	}

	private Object getOpenAPISchema(String name) {
		if (name.endsWith(".yml") || name.endsWith(".yaml")) {
			setYAMLMIMEType();
			return JSONTool.getYAMLASCIILogo(null) + JSONTool.toYAML(openapi());
		}
		return openapi();
	}

	@Override
	public Object get(Parameters params) throws HTTPException {
		List<String> parts = params.getURIParts(getName());

		if (parts.isEmpty())
			return notFound("No object");

		if (parts.get(0).startsWith("openapi"))
			return getOpenAPISchema(parts.get(0));

		String object = parts.get(0);
		if (!getSettings().has(object))
			return notFound("Unknown object: " + object);

		try {
			if (parts.size() == 1)
				return success(searchObjectRecords(object));
			else
				return success(getObjectRecord(object, parts.get(1)));
		} catch (SearchException e) {
			return error(e);
		}
	}

	@Override
	public Object post(Parameters params) throws HTTPException {
		List<String> parts = params.getURIParts(getName());

		if (parts.isEmpty())
			return notFound("No object");

		String object = parts.get(0);
		if (!getSettings().has(object))
			return notFound("Unknown object: " + object);

		try {
			return success(searchObjectRecords(object, params.getJSONObject()));
		} catch (SearchException e) {
			return error(e);
		}
	}
}
