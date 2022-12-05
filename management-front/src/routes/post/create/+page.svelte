<script>
    import Editor from 'tui-editor-svelte/Editor.svelte';
    import '@toast-ui/editor/dist/toastui-editor-viewer.css';
    import {postDataWithHost, serverHost} from "$lib/common.js";
    import CategoryChoice from "../../category/CategoryChoice.svelte";

    import { assets, base } from '$app/paths';

    function submit() {
        postDataWithHost("/licence")
    }


    let selectedCategoryIdSet = new Set();
    selectedCategoryIdSet.add("cook")

    let hooks = {
        hooks: {
            addImageBlobHook: (blob, callback) => {
                // blob : Java Script 파일 객체
                console.log(blob);
                const formData = new FormData();
                formData.append('image', blob);
                formData.append('id', "3");
                let url = '/image/';
                fetch(serverHost + "/image", {
                    method: "POST",
                    credentials: "include",
                    body: formData
                }).then((response) => response.json())
                    .then((data) => {
                        callback(data.result, "test");
                    }).catch(e => {
                    callback("test", "test");

                })
            }
        }
    }

    console.log("23333")
    console.log(assets)
    console.log(base)
    console.log("23333")


</script>

<svelte:head>
    <title>Create Post</title>
    <meta name="description" content="Post create"/>
</svelte:head>

<template>
    <div class="mb-5">
        <CategoryChoice bind:selectedCategoryIdSet={selectedCategoryIdSet}/>
    </div>
    <div id="post-properties" class="grid grid-cols-1 text-xl">
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="isPublished" class=""> isPublished</label>
                <label for="isPublished" class="px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="checkbox" name="isPublished" id="isPublished"
                       class="col-span-1 border-black border scale-150">
            </div>

        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="title" class="self-center"> title</label>
                <label for="title" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="title" id="title"
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="description" class="self-center"> description</label>
                <label for="description" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="description" id="description"
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="tagList" class="self-center"> tagList</label>
                <label for="tagList" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="tagList" id="tagList"
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="ogKeywordList" class="self-center"> og keyword list</label>
                <label for="ogKeywordList" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="ogKeywordList" id="ogKeywordList"
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
    </div>
    {assets}
    {base}
    <Editor initialValue="# 제목" height="80vh" lass="" options={hooks}/>
    <div class="p-2 w-full">
        <div class="flex">
            <button id="cancel" name="memo"
                    class="w-1/2 gray-900 border-gray-400 text-gray-400 hover:bg-green-200 hover:text-white border-green400 text-white py-2 px-4 rounded mr-2">
                '취소'
            </button>
            <button id="submit" name="submit" on:click={submit}
                    class="w-1/2 bg-green-400 hover:bg-green-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2">
                '등록'
            </button>
        </div>
    </div>
</template>