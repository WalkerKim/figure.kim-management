<script>
    import '@toast-ui/editor/dist/toastui-editor-viewer.css';
    import MdEditor from "../../MdEditor.svelte";
    import {postDataWithUrl, putDataWithUrl} from "$lib/common.js";
    import {goto} from "$app/navigation";

    export let data;
	let editor;

	function submit() {
		data.rawContent = editor.invoke("getMarkdown");
		data.renderedContent = editor.invoke("getHtml");
		data.contentFormat = "MARKDOWN"
		if(data.isDraft){
			postDataWithUrl("/content", data, true).then(res=>{
				res.json().then(content => goto("/post/edit/"+content.id))
			});
		}else{
			putDataWithUrl("/content/"+data.id, data, true).then(res=>{
				location.reload()
			})
		}
	}
	function tempSubmit() {
		data.rawContent = editor.invoke("getMarkdown");
		data.renderedContent = editor.invoke("getHtml");
		data.contentFormat = "MARKDOWN"
		putDataWithUrl("/content/"+data.id, data, true);
	}

</script>

<svelte:head>
	<title>Edit Post</title>
	<meta name="description" content="Post edit - Figure.kim" />
</svelte:head>

<template>
	<h1 class="text-3xl">콘텐츠 수정</h1>
	<MdEditor bind:editor bind:content={data}></MdEditor>
	<div class="mb-40 p-2 w-full">
		<div class="flex">
			{#if data.isDraft}
				<button id="temp-store" name="temp-store" on:click={tempSubmit}
						class="w-1/2 bg-blue-400 hover:bg-blue-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2">
					임시 저장
				</button>
			{/if}
			<button id="submit" name="submit" on:click={submit}
					class="w-1/2 bg-blue-400 hover:bg-blue-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2">
				저장
			</button>
		</div>
	</div>
</template>