<script>
    import '@toast-ui/editor/dist/toastui-editor-viewer.css';
    import PostGrid from "../../post/PostGrid.svelte";

    /** @type {import('./$types').PageData} */
	export let data;
	console.log(data)
	console.log(data)
	let contentUrl = "/tag/"+data.id+"/content";

</script>

<svelte:head>
	<title>Tag - content</title>
	<meta name="description" content="Post edit - Figure.kim" />
</svelte:head>

<template>
	<h1 class="text-3xl">Tag : {data.id}</h1>
	<PostGrid bind:url={contentUrl}></PostGrid>

</template>