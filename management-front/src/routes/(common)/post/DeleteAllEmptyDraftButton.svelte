<script>
    import {deleteDataWithUrl} from "$lib/common.js";

    export function deleteAllEmptyDraftPosts(){
        deleteDataWithUrl("/content/draft",null, true).then(res=>{
            location.reload(true);
        })
    }
</script>
<template>
    <button on:click={deleteAllEmptyDraftPosts} class="hidden sm:inline-flex bg-red-500 hover:bg-red-600 border-red-500 text-white py-2 px-4 border-0 rounded w-auto whitespace-nowrap">delete all empty draft post</button>
</template>