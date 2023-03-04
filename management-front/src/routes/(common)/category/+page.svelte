<script>
    import CategoryChoice from "./CategoryChoice.svelte";
    import {deleteDataWithHost, postDataWithHost} from "$lib/common.js";

    let selectedCategoryIdArray = new Array();
    function deleteSelectedCategory(){
        selectedCategoryIdArray.forEach(id=>{
            deleteDataWithHost("/category/"+id, null, true).then(i=>location.reload());
        })
    }

    function submitParentCategory(){
        console.log(parentCategory)
        postDataWithHost("/category", parentCategory, true).then(i=>location.reload());

    }
    function submitChildCategory(){
        postDataWithHost("/category", childCategory, true).then(i=>location.reload());

    }

    let parentCategory = new Object({"depth":0});
    let childCategory = new Object({"depth":1});


</script>

<template>
    <button on:click={deleteSelectedCategory} class="hidden sm:inline-flex bg-red-500 hover:bg-red-600 border-red-500 text-white px-2  py-1 rounded w-auto whitespace-nowrap">delete selected category</button>
    <CategoryChoice bind:selectedCategoryIdArray/>
    <div class="container my-3">
        <h2 class="text-2xl">Add Parent Category</h2>

        <label for="parent-id">ID</label>
        <input type="text" bind:value={parentCategory.id} class="border-black border-2" id="parent-id">
        <label for="parent-name">Name</label>
        <input type="text" bind:value={parentCategory.name} class="border-black border-2" id="parent-name">
        <label for="parent-key">URI Key</label>
        <input type="text" bind:value={parentCategory.uriKey} class="border-black border-2" id="parent-key">
        <button class="border-black border-2 p-1" on:click={submitParentCategory}>submit</button>
    </div>
    <hr>
    <div class="container my-3">
        <h2 class="text-2xl">Add Child Category</h2>

        <label for="child-id">ID</label>
        <input type="text" bind:value={childCategory.id} class="border-black border-2" id="child-id">
        <label for="child-name">Name</label>
        <input type="text" bind:value={childCategory.name} class="border-black border-2" id="child-name" >
        <label for="child-key">URI Key</label>
        <input type="text" bind:value={childCategory.uriKey} class="border-black border-2" id="child-key" >
        <label for="child-parent-id">Parent ID</label>
        <input type="text" bind:value={childCategory.parentCategoryId} class="border-black border-2" id="child-parent-id">
        <button class="border-black border-2 p-1" on:click={submitChildCategory}>submit</button>
    </div>
    <hr>

</template>