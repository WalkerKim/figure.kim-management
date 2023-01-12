<script>
    export let content;
    let tagListString = Array.isArray(content.tagList) ? content.tagList.map(i=>i.id).join(",") : "";
    let ogKeywordListString = Array.isArray(content.ogKeywordList) ? content.ogKeywordList.join(",") : "";
    let recommendPostIdList = Array.isArray(content.recommendPostIdList) ? content.recommendPostIdList.join(",") : "";
    $: content.tagList = stringToArray(tagListString);
    $: content.ogKeywordList = stringToArray(ogKeywordListString);
    $: content.recommendPostIdList = stringToArray(recommendPostIdList);

    function stringToArray(inputString){
        return [... new Set(inputString.replaceAll(" ", "").split(",").filter(string=>string!=""))]
    }
</script>
<template>
    <div id="post-properties" class="grid grid-cols-1 text-xl">
        <h1 class="text-2xl">ID : {content.id}</h1>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="isPublished" class=""> isPublished</label>
                <label for="isPublished" class="px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="checkbox" name="isPublished" id="isPublished" bind:checked={content.isPublished}
                       class="col-span-1 border-black border scale-150">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="isPublished" class=""> recommendStat</label>
                <label for="isPublished" class="px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="number" name="recommendStat" id="recommendStat" bind:value={content.recommendStat}
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="title" class="self-center"> title</label>
                <label for="title" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="title" id="title" bind:value={content.title}
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="description" class="self-center"> description</label>
                <label for="description" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="description" id="description" bind:value={content.description}
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="tagList" class="self-center"> tagList</label>
                <label for="tagList" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="tagList" id="tagList" bind:value={tagListString}
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="ogKeywordList" class="self-center"> og keyword list</label>
                <label for="ogKeywordList" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="ogKeywordList" id="ogKeywordList" bind:value={ogKeywordListString}
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="ogKeywordList" class="self-center"> recommend Post Id List</label>
                <label for="ogKeywordList" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="recommendPostIdList" id="recommendPostIdList" bind:value={recommendPostIdList}
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>
        <hr>
        <div class="col-span-1 py-1 my-2 flex">
            <div class="w-36 justify-between flex">
                <label for="ogKeywordList" class="self-center"> og image</label>
                <label for="ogKeywordList" class="self-center px-3"> : </label>
            </div>
            <div class="w-full">
                <input type="text" name="ogImage" id="ogImage" bind:value={content.ogImage}
                       class="col-span-1 border-black border w-full shadow appearance-none border rounded py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
            </div>
        </div>

    </div>
</template>