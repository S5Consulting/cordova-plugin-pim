/*global cordova, module*/

module.exports = {
    open: function (data, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Pim", "open", [data]);
    },
    start: function (data, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Pim", "trans", [data]);
    },
    registerPayPointEventCallback: function(callback) {
        cordova.exec(callback, null, "Pim", "event.callback", []);
    }
};
