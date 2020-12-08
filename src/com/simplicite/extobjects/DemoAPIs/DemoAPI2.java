package com.simplicite.extobjects.DemoAPIs;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.simplicite.util.ObjectDB;
import com.simplicite.util.exceptions.HTTPException;
import com.simplicite.util.exceptions.SearchException;
import com.simplicite.util.tools.Parameters;

public class DemoAPI2 extends com.simplicite.webapp.services.RESTServiceExternalObject {
	private static final long serialVersionUID = 1L;

	private static JSONObject config = null;
	
	@Override
	public void init(Parameters params) {
		config = getGrant().getJSONObjectParameter("DEMO_API2_CONFIG");
	}

	@Override
	public Object get(Parameters params) throws HTTPException
	{
		ObjectDB obj = null;
		try {
			List<String> parts = params.getURIParts(getName());
			
			if (parts.size() == 0)
				return notFound("No object");

			String objName = parts.get(0);
			if (!config.has(objName))
				return notFound("Unknown object: " + objName);

			obj = borrowAPIObject(objName); // borrow an API object instance from the pool

			if (parts.size() == 1) { // Search
				List<String[]> rows = obj.getTool().search(params.getParameters()); // filtered search

				// Remove row IDs
				JSONArray list = new JSONArray(obj.toJSON(rows, null, false, true, false));
				for (int i = 0; i < list.length(); i++)
					list.getJSONObject(i).remove("row_id");

				return list;
			} else { // Select from the custom field
				String value = parts.get(1);
				String field = config.getString(objName);
				List<String[]> rows = obj.getTool().search(new JSONObject().put(field, value)); // filtered search using reference only

				// No unique record matching the field value => not found
				if (rows.size() != 1)
					return notFound("No " + objName + " for " + field + " = " + value);

				// Remove row ID
				JSONObject item = new JSONObject(obj.toJSON(rows.get(0), null, false, true));
				item.remove("row_id");

				return item;
			}
		} catch (SearchException e) {
			return error(e);
		} finally {
			if (obj != null)
				returnAPIObject(obj); // return the API object intance to the pool
		}
	}
}
