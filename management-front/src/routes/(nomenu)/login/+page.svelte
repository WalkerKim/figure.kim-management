<script>
    import {postDataWithHost} from "$lib/common.js";
    import {goto} from "$app/navigation";

    let result = null
    let errorMessage;
    let errorBool = false;
    let username, password
    const onKeyUp = e => {
        if (e.keyCode === 13) {
            signIn();
        }
    };

    // const passwordForgotClick = e=>{
    //     alert(passwordForgotAlertMessage)
    // }


    function signIn() {
        let signInObject = {};
        signInObject["username"] = username;
        signInObject["password"] = password;

        try {
            postDataWithHost("/login", signInObject)
                .then(res => {
                    goto("/post");
                }).catch(reason => {
                errorBool = true;
                errorMessage = reason.message??"Network Error";
                return false;
            }).then(result => {
                console.log("result", result )
                if (result) {
                    console.log("login success")
                }else{
                    console.log("login failed")
                }

            })
        } catch (e) {
            console.error(e)
        }



    }

</script>
<div class="flex w-screen h-screen mx-0">
    <div class="bg-black sm:w-1/3 flex w-full h-full items-center sm:mt-0 -mt-10">
        <div class="bg-transparent rounded-lg flex flex-col md:mr-auto w-full sm:mt-10 mt-0">
            <div class="sm:w-2/3 w-full self-center sm:p-0 px-4">
                <h2 class="text-white text-2xl font-medium title-font mb-3 text-center sm:text-left">도형킴 Management</h2>
                <h2 class="text-white sm:text-6xl text-5xl font-bold title-font sm:mb-14 mb-10 text-center sm:text-left">Sign in</h2>
                <div class="relative sm:mb-2 mb-8">
                    <label for="full-name" class="leading-7 text-sm text-white">ID</label>
                    <input type="email" id="full-name" name="full-name" required bind:value={username}
                           on:keyup={onKeyUp}
                           class="w-full bg-transparent rounded border border-gray-500 focus:border-gray-600 focus:ring-2 focus:ring-gray-600 text-base outline-none text-white py-1 px-3 leading-10 transition-colors duration-200 ease-in-out">
                </div>
                <div class="relative mb-1">
                    <label for="email" class="leading-7 text-sm text-white">Password</label>
                    <input type="password" id="email" name="email" required bind:value={password} on:keyup={onKeyUp}
                           class="w-full bg-transparent rounded border border-gray-500 focus:border-gray-600 focus:ring-2 focus:ring-gray-600 text-base outline-none text-white py-1 px-3 leading-10 transition-colors duration-200 ease-in-out">
                </div>
                <div class="sm:mb-3 grid sm:justify-items-end justify-items-left sm:mt-2 mt-4 mb-8">
                    <!--                TODO-->
                    <div>
<!--                        <span on:click={passwordForgotClick} class="block no-underline text-white hover:underline text-sm hover:cursor-pointer"-->
<!--                           style="color:darkgray">Forgot password</span>-->
                    </div>
                </div>
                <div class="mb-3 grid justify-items-end">
                    {#if errorBool}
                        <p class="text-red-600">{errorMessage}</p>
                    {/if}

                </div>
                <button on:click={signIn}
                        class="w-full text-custom-black-900 hover:text-custom-black-100 bg-custom-black-100 border-0 py-2 px-8 focus:outline-none hover:bg-custom-black-900 rounded text-lg leading-9">
                    Sign in
                </button>
            </div>
            <div class="bg-transparent rounded-lg flex flex-col md:mr-auto w-full sm:mt-5 mt-2 align-middle items-center">
<!--                <p class="text-sm font-bold text-white ">version - {pjson.version}</p>-->
            </div>
        </div>
    </div>
    <div class="sm:w-2/3 bg-gradient-to-r from-custom-black-900 to-custom-black-100 w-full h-full items-center sm:flex justify-center hidden">
        <div class="mb-16">
            <p class="text-6xl text-white font-light self-center mb-3">Welcome to</p>
            <p class="text-7xl font-bold text-white">Figure.Kim Management System</p>
        </div>
    </div>
</div>
