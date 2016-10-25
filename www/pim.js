
// ForhandlerID TEST 88008800
/*global cordova, module*/

module.exports = {
    open: function (ip, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Pim", "open", [ip]);
    },
    regEventCallback: function(callback) {
        cordova.exec(callback, null, "Pim", "callback", []);
    },
    startTrans: function (amount, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Pim", "trans", [amount]);
    },
    print: function (print, successCallback, errorCallback) {
    	cordova.exec(successCallback, errorCallback, "Pim", "print", [print]);
    }
};
