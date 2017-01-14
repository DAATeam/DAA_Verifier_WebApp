/**
 * Created by DK on 11/27/16.
 */
const appPort = '6970';
const verifierDomain = 'localhost';
const localAppUrl = 'http://localhost:'+appPort+'/';
const verifierUrl = 'http://'+verifierDomain+':8090/';
function loginService(appId, pass, cb) {
    var urlLogin = verifierUrl+'login';
    $.ajax({
        url: urlLogin,
        type: "post",
        success: function(data) {
            console.log(data);
            cb(true, data);
        },
        error: function (xhr, thrownError) {
            console.log(thrownError);
            cb(false);
        },
        data: {
            app_id: parseInt(appId, 10),
            m: pass
        }
    });
}