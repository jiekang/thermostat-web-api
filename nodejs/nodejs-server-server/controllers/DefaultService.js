'use strict';

var MongoClient = require('mongodb').MongoClient,
    assert = require('assert');

var url = 'mongodb://mongodevuser:mongodevpassword@localhost:27518/thermostat';
var db;
MongoClient.connect(url, function(err, mongodb) {
  assert.equal(null, err);
  db = mongodb;
});

exports.namespaceSystemSystemIdAgentsAgentIdJvmsDELETE = function(args, res, next) {
  /**
   * Delete JVM information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemSystemIdAgentsAgentIdJvmsGET = function(args, res, next) {
  /**
   * Get JVM information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * limit Integer Limit of items to return. (optional)
   * offset Integer Offset of items to return. (optional)
   * sort String Sort string (optional)
   * returns jvm-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemSystemIdAgentsAgentIdJvmsPOST = function(args, res, next) {
  /**
   * Query JVM information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * limit Integer Limit of items to return. (optional)
   * offset Integer Offset of items to return. (optional)
   * sort String Sort string (optional)
   * filters Filters Key, Comparator, Value trios to filter the data (optional)
   * returns jvm-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemSystemIdAgentsAgentIdJvmsPUT = function(args, res, next) {
  /**
   * Adds or updates JVM information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * jvmInfo Jvm-info The jvm information
   * tags String CSV of tags (optional)
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsDELETE = function(args, res, next) {
  /**
   * Delete system information
   *
   * namespace String The namespace
   * systemIds String A list of system identities
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsGET = function(args, res, next) {
  /**
   * Get system information
   *
   * namespace String The namespace
   * limit Integer Limit of items to return. (optional)
   * offset Integer Offset of items to return. (optional)
   * sort String Sort string (optional)
   * returns system-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemsPOST = function(args, res, next) {
  /**
   * Query system information
   *
   * namespace String The namespace
   * limit Integer Limit of items to return. (optional)
   * offset Integer Offset of items to return. (optional)
   * sort String Sort string (optional)
   * filters Filters Key, Comparator, Value trios to filter the data (optional)
   * returns system-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemsPUT = function(args, res, next) {
  /**
   * Add or update system information
   *
   * namespace String The namespace
   * systemInfo System-info The system information
   * tags String CSV of tags (optional)
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsSystemIdAgentsAgentIdDELETE = function(args, res, next) {
  /**
   * Delete agent information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsSystemIdAgentsAgentIdGET = function(args, res, next) {
  /**
   * Get agent information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * returns agent-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemsSystemIdAgentsAgentIdJvmsJvmIdDELETE = function(args, res, next) {
  /**
   * Delete JVM information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * jvmId String The JVM identity
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsSystemIdAgentsAgentIdJvmsJvmIdGET = function(args, res, next) {
  /**
   * Get JVM information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * jvmId String The JVM identity
   * returns jvm-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemsSystemIdAgentsAgentIdJvmsJvmIdPUT = function(args, res, next) {
  /**
   * Adds or updates JVM information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * jvmId String The JVM identity
   * jvmInfo Jvm-info The jvm information
   * tags String CSV of tags (optional)
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsSystemIdAgentsAgentIdPUT = function(args, res, next) {
  /**
   * Adds or updates agent information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentId String The agent identity
   * agentInfo Agent-info The agent information
   * tags String CSV of tags (optional)
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsSystemIdAgentsDELETE = function(args, res, next) {
  /**
   * Delete agent information
   *
   * namespace String The namespace
   * systemId String The system identity
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsSystemIdAgentsGET = function(args, res, next) {
  /**
   * Get agent information
   *
   * namespace String The namespace
   * systemId String The system identity
   * limit Integer Limit of items to return. (optional)
   * offset Integer Offset of items to return. (optional)
   * sort String Sort string (optional)
   * returns agent-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemsSystemIdAgentsPOST = function(args, res, next) {
  /**
   * Query agent information
   *
   * namespace String The namespace
   * systemId String The system identity
   * limit Integer Limit of items to return. (optional)
   * offset Integer Offset of items to return. (optional)
   * sort String Sort string (optional)
   * filters Filters Key, Comparator, Value trios to filter the data (optional)
   * returns agent-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemsSystemIdAgentsPUT = function(args, res, next) {
  /**
   * Adds or updates agent information
   *
   * namespace String The namespace
   * systemId String The system identity
   * agentInfo Agent-info The agent information
   * tags String CSV of tags (optional)
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsSystemIdDELETE = function(args, res, next) {
  /**
   * Delete system information
   *
   * namespace String The namespace
   * systemId String The system identity
   * no response value expected for this operation
   **/
  res.end();
}

exports.namespaceSystemsSystemIdGET = function(args, res, next) {
  /**
   * Get system information
   *
   * namespace String The namespace
   * systemId String The system identity
   * returns system-info
   **/
  var examples = {};
  examples['application/json'] = "";
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

exports.namespaceSystemsSystemIdPUT = function(args, res, next) {
  /**
   * Add or update system information
   *
   * namespace String The namespace
   * systemId String The system identity
   * systemInfo System-info The system information
   * tags String CSV of tags (optional)
   * no response value expected for this operation
   **/
  res.end();
}

exports.rootGET = function(args, res, next) {
  /**
   * Get namespaces
   *
   * returns namespaces
   **/
  var examples = {};
  examples['application/json'] = {
  "namespaces" : [ "aeiou" ]
};

  var collection = db.collection('test');
  collection.insertMany([
    {a : 1}, {a : 2}, {a : 3}
  ], function(err, result) {
    assert.equal(err, null);
    assert.equal(3, result.result.n);
    assert.equal(3, result.ops.length);
  });
  if (Object.keys(examples).length > 0) {
    res.setHeader('Content-Type', 'application/json');
    res.end(JSON.stringify(examples[Object.keys(examples)[0]] || {}, null, 2));
  } else {
    res.end();
  }
}

