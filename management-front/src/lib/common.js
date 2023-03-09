export const serverHost = import.meta.env.VITE_BACKEND_ADDR ?? "http://localhost:8888";

export function deleteDataWithUrl(url, bodyData, autoAlertBool) {
    return fetchWithMethod("DELETE", url, bodyData).then(res => {
            console.log(res)
            return autoAlertFunc(res, autoAlertBool)
        }
    );
}

export function putDataWithUrl(url, bodyData, autoAlertBool) {
    return fetchWithMethod("PUT", url, bodyData).then(res => {
            console.log(res)
            return autoAlertFunc(res, autoAlertBool)
        }
    );
}

export function postDataWithUrl(url, bodyData, autoAlertBool) {
    return fetchWithMethod("POST", url, bodyData).then(res => {
            console.log(res)
            return autoAlertFunc(res, autoAlertBool)
        }
    );
}

export function getDataWithUrl(url, autoAlertBool) {
    console.log(url)
    return fetch(serverHost + url, {
        credentials: "include",
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(res => autoAlertFunc(res, autoAlertBool));
}


export function fetchWithMethod(method, url, bodyData) {
    console.log(serverHost + url)
    return fetch(serverHost + url, {
        method: method,
        credentials: "include",
        headers: {
            'Content-Type': 'application/json'
        }
        ,
        body: JSON.stringify(bodyData)
    })
}

export async function testSession() {
    return fetch(serverHost + "/session-test", {
        method: "GET",
        credentials: "include",
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

function autoAlertFunc(res, autoAlertBool) {
    if (res.status > 100 && res.status < 300) {
        if (autoAlertBool) {
            alert('Request complete.');
        }
        // return res.json();
        let resultTextPromise = res.text();
        return resultTextPromise.then(text => {
            if(text){
                return JSON.parse(text)
            }else{
                return;
            }
        });
    } else {
        return res.json().then(i => {
            if (autoAlertBool) {
                if (res.status == 403) {
                    alert('권한 없음');
                } else if (res.status == 401) {
                    alert('로그인 정보 없음');
                } else {
                    alert(i.defaultMessage)
                }
            }
            throw new Error(i.defaultMessage);
        });

    }

}