<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![](https://docs.simplicite.io//logos/logo250.png)
* * *

`DemoAPIs` module definition
============================

### Introduction

This module contains the following custom APIs for the demo **order management** application.:

- A standard mapped API for various business object
- A custom API for the product business object

### Prerequisites

The `Demo` module **must** be installed and configured before importing this addon module.

### Import

To import this module:

- Create a module named `DemoAddons`
- Set the settings as:

```json
{
	"type": "git",
	"origin": {
		"uri": "https://github.com/simplicitesoftware/module-demo-apis.git"
	}
}
```

- Click on the _Import module_ button

### Configure

Custom URIs can be configured in the `URI_MAPPINGS` system parameter:

```json
[
	(...)
	{ "source": "^/demo/api/v1(.+)$", "destination": "/api/ext/DemoAPI1$1" },
	{ "source": "^/demo/api/v2(.+)$", "destination": "/api/ext/DemoAPI2$1" },
	(...)
]
```

`DemoAPI1` external object definition
-------------------------------------

Custom REST web services


`DemoAPI2` external object definition
-------------------------------------

Custom REST web services


