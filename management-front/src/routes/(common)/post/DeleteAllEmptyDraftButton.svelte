<script>
    import {deleteDataWithHost} from "$lib/common.js";

    export function deleteAllEmptyDraftPosts(){
        deleteDataWithHost("/content/draft").then(res=>{
            location.reload(true);
        })
    }
</script>
<template>
    <a on:click={deleteAllEmptyDraftPosts} class="hidden sm:inline-flex bg-red-500 hover:bg-red-600 border-red-500 text-white py-2 px-4 border-0 rounded w-auto whitespace-nowrap">delete all empty draft post</a>
</template>