![Logo](https://platform.simplicite.io/logos/standard/logo250.png)
* * *

Introduction
------------

This module contains the following custom APIs for the demo **order management** application.:

- A **mapped** API for various business object
- A **custom** API for the product business object

Prerequisites
-------------

The `Demo` module **must** be installed and configured before importing this addon module.

Import
------

To import this module:

- Create a module named `DemoAddons`
- Set the settings as:

```json
{
	"type": "git",
	"branch": "v6",
	"origin": { "uri": "https://github.com/simplicitesoftware/module-demo-apis.git" }
}
```

- Click on the _Import module_ button

Configure
---------

Custom URIs can be configured in the `URI_MAPPINGS` system parameter:

```json
[
	(...)
	{ "source": "^/demo/api/v1(.+)$", "destination": "/api/ext/DemoAPI1$1" },
	{ "source": "^/demo/api/v2(.+)$", "destination": "/api/ext/DemoAPI2$1" },
	(...)
]
```

Usage
-----

In both cases you need to obtain an authentication token:

```text
TOKEN=$(curl -s -u <username>:<password> "<base URL>/api/login")
```

### Mapped API usage

Get the OpenAPI schema:

```text
curl -s -H "Authorization: Bearer $TOKEN" "<base URL>/api/ext/DemoAPI1/openapi.<json|yaml>"
```

Get a list of mapped object (e.g. `suppliers`)

```text
curl -s -H "Authorization: Bearer $TOKEN" "<base URL>/api/ext/DemoAPI1/suppliers"
```

### Custom API usage

Get the OpenAPI schema:

```text
curl -s -H "Authorization: Bearer $TOKEN" "<base URL>/api/ext/DemoAPI2/openapi.<json|yaml>"
```

Get a list of all products:

```text
curl -s -H "Authorization: Bearer $TOKEN" "<base URL>/api/ext/DemoAPI2/DemoProduct"
```

Get a single product with its reference (e.g. `REF002`):

```text
curl -s -H "Authorization: Bearer $TOKEN" "<base URL>/api/ext/DemoAPI2/DemoProduct/REF002"
```

Search for products passing filters as JSON body
(e.g. search for the available products supplied by the `BIM` supplier):

```text
curl -s -H "Authorization: Bearer $TOKEN" \
    -H 'Content-Type: application/json' \
    -d '{ "demoPrdSupId__demoSupCode": "BIM", "demoPrdAvailable": true }' \
    "<base URL>/api/ext/DemoAPI2/DemoProduct"
```

> **Note** the `Content-Type: application/json` header is **mandatory** for the 
> body to be parsed as JSON.

