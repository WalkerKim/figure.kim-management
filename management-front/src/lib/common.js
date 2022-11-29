export const serverHost = "http://localhost:8888";

export function deleteDataWithHost(url, bodyData, autoAlertBool){
    return fetchWithMethod("DELETE", url, bodyData).then(res => autoAlertFunc(res, autoAlertBool));
}
export function putDataWithHost(url, bodyData, autoAlertBool){
    return fetchWithMethod("PUT", url, bodyData).then(res => autoAlertFunc(res, autoAlertBool));
}
export function postDataWithHost(url, bodyData, autoAlertBool){
    return fetchWithMethod("POST", url, bodyData).then(res => autoAlertFunc(res, autoAlertBool));
}
export function getDataWithHost(url, autoAlertBool){
    console.log(url)
    return fetch(serverHost + url, {
        method: "GET",
        credentials: "include",
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(res => autoAlertFunc(res, autoAlertBool));
}

function fetchWithMethod(method, url, bodyData){
    console.log(serverHost + url)
    return fetch(serverHost + url, {
        method: method,
        credentials: "include",
        headers: {
            'Content-Type': 'application/json',
        }
        ,
        body: JSON.stringify(bodyData)
    });
}

function autoAlertFunc(res, autoAlertBool){
    console.log(res)
    if(autoAlertBool){
        if(res.status>100&&res.status<300){
            alert('Request complete.');
        }else{
            res.json().then(i => alert(i.message));
        }
    }else{

    }
    return res;
}