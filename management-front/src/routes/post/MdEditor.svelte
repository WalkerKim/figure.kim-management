<script>
    import CategoryChoice from "../category/CategoryChoice.svelte";
    import ContentProps from "./ContentProps.svelte";
    import {serverHost} from "$lib/common.js";
    import Editor from 'tui-editor-svelte/Editor.svelte';
    import '@toast-ui/editor/dist/toastui-editor-viewer.css';

    export let content = new Object();
    export let editor;
    let selectedCategoryIdSet = new Set();
    $:content["categoryList"] = [... selectedCategoryIdSet];
    let hooks = {
        hooks: {
            addImageBlobHook: (blob, callback) => {
                // blob : Java Script 파일 객체
                const formData = new FormData();
                formData.append('image', blob);
                formData.append('id', content.id);
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
</script>
<template>
    <div class="mb-5">
        <CategoryChoice bind:selectedCategoryIdSet={selectedCategoryIdSet} isActiveAutoSelectParent={true}/>
    </div>
    <ContentProps bind:content={content}></ContentProps>
    <Editor height="80vh" lass="" options={hooks} bind:this={editor}/>
</template>