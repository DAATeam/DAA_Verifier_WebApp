/**
 * Created by DK on 12/9/16.
 */
const appPort = '6970';
const verifierDomain = 'localhost';
const localAppUrl = 'http://localhost:'+appPort+'/';
const verifierUrl = 'http://'+verifierDomain+':8090/';
var sessionId = null;
var servicePageId = null;
function clickAuthen() {
    console.log('welcome');
    process(localAppUrl+'new', 'get', null, getSessionSuccess, communicateFail, 'get sessionId from local App');
}
window.onload = function() {
    if (document.getElementById("button-authen").value == "true") {
        document.getElementById("button-authen").disabled = false;
    }
};
function getSessionSuccess(appData) {
    var serviceId = document.getElementById("serviceId").getAttribute("value");
    servicePageId = serviceId;
    console.log('App sessionId:  ', appData);
    console.log('AppId:  ', serviceId);
    sessionId = appData;
    process(verifierUrl+'getCert/'+serviceId+'/'+appData, 'get', appData, sendVerifierCertToLocalApp,
        communicateFail, 'get certificate from verifier');
}
function sendVerifierCertToLocalApp(verifierData) {
    console.log('data from verifier: ', verifierData);
    process(localAppUrl+'verify', 'post', verifierData, sendAppCertToVerifier, communicateFail, "send verifier certificate to app");
}
function sendAppCertToVerifier(appData) {
    console.log('data from App: ', appData);
    process(verifierUrl+'verify/'+servicePageId+'/'+sessionId, 'post', appData, verifySuccess, communicateFail, 'send app daa to verifier');
}
function verifySuccess(message) {
    console.log('verify Success: ', message);
    document.getElementById("result").className = "authen-success";
    document.getElementById("result").innerText = "Verify User Success!";
}
function communicateFail(xhr, err, message) {
    console.log('xhr: ', xhr);
    console.log('communicate Fail: ', err);
    console.log('step Fail: ', message);
    document.getElementById("result").className = "authen-error";
    document.getElementById("result").innerText = "Verify User Fail!";
}
function process(url, type, data, response, error, step) {
    if (type == "get") {
        $.ajax({
            url: url,
            type: "get",
            success: function(data) {
                response(data);
            },
            error: function (xhr, thrownError) {
                error(xhr, thrownError, step);
            }
        });
    } else {
        $.ajax({
            url: url,
            type: "post",
            success: function(data) {
                response(data);
            },
            error: function (xhr, thrownError) {
                error(xhr, thrownError, step);
            },
            data: data
        });
    }
}

