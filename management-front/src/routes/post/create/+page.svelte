<script>
    import {postDataWithHost, putDataWithHost} from "$lib/common.js";
    import MdEditor from "../MdEditor.svelte";
    import {onMount} from "svelte";
    import {goto} from "$app/navigation";

    let editor, content, id;

    function submit() {
        content.rawContent = editor.invoke("getMarkdown");
        content.renderedContent = editor.invoke("getHtml");
        content.contentFormat = "MARKDOWN"
        postDataWithHost("/content", content, true).then(res=>{
            res.json().then(content => goto("/post/edit/"+content.id))

        });
    }
    function tempSubmit() {
        content.rawContent = editor.invoke("getMarkdown");
        content.renderedContent = editor.invoke("getHtml");
        content.contentFormat = "MARKDOWN"
        putDataWithHost("temp-content", content);
    }

    onMount(async ()=>{
        const res = await postDataWithHost("/temp-content");
        content = await res.json();
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
                    class="w-1/2 bg-green-400 hover:bg-green-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2">
                임시 저장
            </button>
            <button id="submit" name="submit" on:click={submit}
                    class="w-1/2 bg-green-400 hover:bg-green-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2">
                저장
            </button>
        </div>
    </div>
</template>