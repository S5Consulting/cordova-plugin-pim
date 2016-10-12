/*global cordova, module*/

module.exports = {
    open: function (data, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Pim", "open", [data]);
    }
};
