<script>
    import {deleteDataWithHost} from "$lib/common";

    export function deleteAllEmptyDraftPosts(){
        deleteDataWithHost("/content/draft").then(res=>{
            location.reload(true);
        })
    }
</script>
<template>
    <button on:click={deleteAllEmptyDraftPosts} class="hidden sm:inline-flex bg-red-500 hover:bg-red-600 border-red-500 text-white px-2  py-1 rounded w-auto whitespace-nowrap">delete all empty draft post</button>
</template>