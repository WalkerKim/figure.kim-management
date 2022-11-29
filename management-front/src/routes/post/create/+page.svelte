<script>
	import Editor from 'tui-editor-svelte/Editor.svelte';
	import '@toast-ui/editor/dist/toastui-editor-viewer.css';
	import {postDataWithHost} from "$lib/common.js";
	import CategoryChoice from "../../category/CategoryChoice.svelte";
	function submit(){
		postDataWithHost("/licence")
	}

	let selectedCategoryIdSet = new Set();
	selectedCategoryIdSet.add("cook")



	function check(){
		console.log(selectedCategoryIdSet)
		selectedCategoryIdSet.add("java")
	}


</script>

<svelte:head>
	<title>Create Post</title>
	<meta name="description" content="Post create" />
</svelte:head>

<template>
	{selectedCategoryIdSet.size}
	<CategoryChoice bind:selectedCategoryIdSet={selectedCategoryIdSet}/>
	<Editor initialValue="Markdown **rocks!**" height="80vh" lass=""/>
	<div class="p-2 w-full" on:click={check}>
		<div class="flex">
			<button id="cancel" name="memo"
					class="w-1/2 gray-900 border-gray-400 text-gray-400 hover:bg-green-200 hover:text-white border-green400 text-white py-2 px-4 rounded mr-2">'취소'</button>
			<button id="submit" name="submit" on:click={submit}
					class="w-1/2 bg-green-400 hover:bg-green-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2">'등록'</button>
		</div>
	</div>
</template>