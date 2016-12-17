/**
 * Created by DK on 12/9/16.
 */
const localAppUrl = 'http://localhost:6969/';
const verifierUrl = 'http://localhost:8090/';
window.onload = function() {
    process(localAppUrl+'new', 'get', null, getSessionSuccess, communicateFail, 'get sessionId from local App');
};
function getSessionSuccess(appData) {
    process(verifierUrl+'getSession', 'post', appData, sendVerifierCertToLocalApp,
        communicateFail, 'get certificate from verifier');
}
function sendVerifierCertToLocalApp(verifierData) {
    process(localAppUrl+'verify', 'post', verifierData, sendAppCertToVerifier, communicateFail, "send verifier certificate to app");
}
function sendAppCertToVerifier(appData) {
    process(verifierUrl+'verify', 'post', appData, verifySuccess, communicateFail, 'send app daa to verifier');
}
function verifySuccess(message) {
    console.log('verify Success: ', message);
}
function communicateFail(xhr, err, message) {
    console.log('xhr: ', xhr);
    console.log('communicate Fail: ', err);
    console.log('step Fail: ', message);
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

