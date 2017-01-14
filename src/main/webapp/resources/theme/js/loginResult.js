/**
 * Created by DK on 1/14/17.
 */
window.onload = function() {
    var appId = document.getElementById("appId").getAttribute("value");
    getHistory(
        appId,
        function(t, d) {
            var object = JSON.parse(d);
            var list = object.list;
            console.log('json: ', list);
            if (list && list != undefined && list.length > 0) {
                createRow("rowLabel", "No.", "UserInfo", "Result", "Timestamp");
            }
            list.forEach(function(item, id) {
                createRow("row"+id.toString(), id+1, item.userInfo, item.result, item.timestamp);
            });
        }
    );
};

const appPort = '6970';
const verifierDomain = 'localhost';
const localAppUrl = 'http://localhost:'+appPort+'/';
const verifierUrl = 'http://'+verifierDomain+':8090/';
function getHistory(appId, cb) {
    var url = verifierUrl+'history/'+appId;
    $.ajax({
        url: url,
        type: "get",
        success: function(data) {
            cb(true, data);
        },
        error: function (xhr, thrownError) {
            console.log(thrownError);
            cb(false);
        }
    });
};

function refresh() {
    var appId = document.getElementById("appId").getAttribute("value");
    getHistory(
        appId,
        function(t, d) {
            var object = JSON.parse(d);
            var list = object.list;
            console.log('json: ', list);
            if (list && list != undefined && list.length > 0) {
                var myNode = document.getElementById("historyTable");
                while (myNode.firstChild) {
                    myNode.removeChild(myNode.firstChild);
                }
                createRow("rowLabel", "No.", "UserInfo", "Result", "Timestamp");
                list.forEach(function(item, id) {
                    createRow("row"+id.toString(), id+1, item.userInfo, item.result, item.timestamp);
                });
            }
        }
    );
}

function createRow(rowId, Number, userInfo, result, timestamp) {
    var y = document.createElement("TR");
    y.setAttribute("id", rowId);
    y.setAttribute("class", "rowTable");
    document.getElementById("historyTable").appendChild(y);
    var arr = [Number, userInfo, result, timestamp];
    arr.forEach(function(item) {
        var z = document.createElement("TD");
        var t = document.createTextNode(item);
        var editClass = '';
        var cl = "rowItem";
        if (item == userInfo) {
            cl = "infoItem";
        }
        if (item == timestamp) {
            cl = "timeItem";
        }
        if (item == result && typeof result == "boolean") {
            if (result) {
                editClass = ' resultSuccess';
            } else {
                editClass = ' resultFail';
            }
        }
        z.setAttribute("class", cl+editClass);
        z.appendChild(t);
        document.getElementById(rowId).appendChild(z);
    });
}