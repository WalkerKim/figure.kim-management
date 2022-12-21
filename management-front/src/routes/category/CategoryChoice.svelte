<script>
    import {afterUpdate, onMount, tick} from "svelte";
    import {getDataWithHost} from "$lib/common.js";

    export let selectedCategoryIdSet;
    export let isActiveAutoSelectParent
    let categoryArray =[];
    onMount(async () => {
        categoryArray = await getDataWithHost("/category");

        await tick();
        selectedCategoryIdSet.forEach(categoryId => {
            document.getElementById('category-'+categoryId).checked = true
        });
    });
    afterUpdate(()=>{
        // console.log("afterUpdate"+selectedCategoryIdSet)
    });

    function handleParentCheck() {
        if(this.checked){
            document.getElementById('category-' + this.dataset.parentId).checked = true;
            selectedCategoryIdSet.add(this.dataset.categoryId)
                if(isActiveAutoSelectParent){
                    selectedCategoryIdSet.add(this.dataset.parentId)
                }
        }else{
            if(isActiveAutoSelectParent){
                let isCheckedChildExists =Array.from(document.getElementsByClassName(this.dataset.parentId)).filter(dom=>dom.checked).length!=0
                if(!isCheckedChildExists){
                    document.getElementById('category-' + this.dataset.parentId).checked = false;
                    selectedCategoryIdSet.delete(this.dataset.parentId);
                }
            }
            selectedCategoryIdSet.delete(this.dataset.categoryId)
        }

    }
    function handleChildCheck(){
        if(isActiveAutoSelectParent){
            let isCheckedChildExists =Array.from(document.getElementsByClassName(this.dataset.categoryId)).filter(dom=>dom.checked).length!=0
            if(!isCheckedChildExists){
                this.checked = false;
            }else{
                this.checked = true;
            }
        }
        if(this.checked){
            selectedCategoryIdSet.add(this.dataset.categoryId)
        }else{
            selectedCategoryIdSet.delete(this.dataset.categoryId)
        }
    }
</script>
<template>
    <div class="container grid grid-cols-12">

        <div class="col-span-12">
            <h2 class="text-2xl">Categories</h2>
        </div>
        <div class="grid grid-cols-6 col-span-12">
            {#each categoryArray as parentCategory}
                <div class="border-2 col-span-1">
                    <div class="w-full">
                        <input type="checkbox" value="{parentCategory.id}" name="category"
                               id="{'category-'+parentCategory.id}"
                               data-category-id="{parentCategory.id}"
                               on:change={handleChildCheck}
                        >
                        <label for="{'category-'+parentCategory.id}" class="w-full">{parentCategory.name} ({parentCategory.id})</label>
                    </div>
                    {#each parentCategory["childCategoryList"]?parentCategory["childCategoryList"]:[] as childCategory}
                        <div class="w-full pl-2">
                            <input type="checkbox" value="{childCategory.id}" name="category"
                                   id="{'category-'+childCategory.id}"
                                   data-category-id="{childCategory.id}"
                                   data-parent-id="{parentCategory.id}"
                                   class="{parentCategory.id}" on:change={handleParentCheck}>
                            <label for="{'category-'+childCategory.id}" class="pl-1">{childCategory.name} ({childCategory.id})</label>
                        </div>
                    {/each}
                </div>
            {/each}
        </div>
    </div>
</template>
