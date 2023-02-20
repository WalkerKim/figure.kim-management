<script>
    import DeleteAllEmptyDraftButton from "./DeleteAllEmptyDraftButton.svelte";
    import PostGrid from "./PostGrid.svelte";

    let contentUrl = "/content"
</script>
<template>
    <h1 class="text-3xl">Post List</h1>
    <div class="container my-3">
        <a class="inline-flex bg-green-400 hover:bg-green-200 hover:text-white border-green400 text-white py-2 px-4 rounded border-0 ml-2"
           href="/post/create">create</a>
        <DeleteAllEmptyDraftButton></DeleteAllEmptyDraftButton>
    </div>
    <div class="container">
        <PostGrid bind:url={contentUrl}></PostGrid>
    </div>
</template>
