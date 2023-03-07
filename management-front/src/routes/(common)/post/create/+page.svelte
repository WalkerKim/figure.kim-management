<script>
    import {postDataWithUrl, putDataWithUrl} from "$lib/common.js";
    import MdEditor from "../MdEditor.svelte";
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";

    let editor, id;
    let content = {};

    function submit() {
        content.rawContent = editor.invoke("getMarkdown");
        content.renderedContent = editor.invoke("getHtml");
        content.contentFormat = "MARKDOWN"
        content.tagList = content.tagList ?? [];
        content.ogKeywordList = content.ogKeywordList ?? [];
        console.log(content);
        // return
        postDataWithUrl("/content", content, true).then(res=>{
            res.json().then(content => goto("/post/edit/"+content.id))
        });
    }
    function tempSubmit() {
        content.rawContent = editor.invoke("getMarkdown");
        content.renderedContent = editor.invoke("getHtml");
        content.contentFormat = "MARKDOWN"
        putDataWithUrl("/content/"+content.id, content);
    }

    onMount(async ()=>{
        const res = await postDataWithUrl("/temp-content");
        console.log(res)
        content = await res;
    })

</script>

<svelte:head>
    <title>Create Post</title>
    <meta name="description" content="Post create"/>
</svelte:head>

<template>
    <h1 class="text-3xl">콘텐츠 생성</h1>
    <MdEditor bind:editor bind:content></MdEditor>
    <div class="p-2 w-full">
        <div class="flex">
            <button id="temp-store" name="temp-store" on:click={tempSubmit}
                    class="w-1/2 bg-blue-400 hover:bg-blue-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2">
                임시 저장
            </button>
            <button id="submit" name="submit" on:click={submit}
                    class="w-1/2 bg-blue-400 hover:bg-blue-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2">
                저장
            </button>
        </div>
    </div>
</template>