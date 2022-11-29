<script>
	import Editor from 'tui-editor-svelte/Editor.svelte';
	import Viewer from 'tui-editor-svelte/Viewer.svelte'
	import '@toast-ui/editor/dist/toastui-editor-viewer.css';
	export let data;
	console.log(data)
</script>

<svelte:head>
	<title>About</title>
	<meta name="description" content="About this app" />
</svelte:head>

<template>
	<Editor initialValue={data.content} height="100vh" lass=""/>
</template>