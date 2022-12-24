<script>
    import CategoryChoice from "../category/CategoryChoice.svelte";
    import ContentProps from "./ContentProps.svelte";
    import {serverHost} from "$lib/common.js";
    import Editor from 'tui-editor-svelte/Editor.svelte';
    import '@toast-ui/editor/dist/toastui-editor-viewer.css';

    export let content = new Object();
    export let editor;
    let selectedCategoryIdSet = new Set();
    $:content["categoryList"] = [... selectedCategoryIdSet];
    let options = {
        hooks: {
            addImageBlobHook: (blob, callback) => {
                // blob : Java Script 파일 객체
                const formData = new FormData();
                formData.append('image', blob);
                formData.append('id', content.id);
                fetch(serverHost + "/image", {
                    method: "POST",
                    credentials: "include",
                    body: formData
                }).then((response) => response.json())
                    .then((data) => {
                        callback(data.result, "test");
                    }).catch(e => {
                    callback("test", "test");
                })
            }
        },
        customHTMLRenderer : {
            heading(node, context) {
                return {
                    type: context.entering ? 'openTag' : 'closeTag',
                    tagName: `h${node.level}`,
                    id: [`heading-'${node.level}`],
                    attributes:{
                        id:node.firstChild.literal
                    }
                }
            },
        }

    }


</script>
<template>
    <div class="mb-5" on:click={()=>    console.log(editor)}>
        <CategoryChoice bind:selectedCategoryIdSet={selectedCategoryIdSet} isActiveAutoSelectParent={true}/>
    </div>
    <ContentProps bind:content={content}></ContentProps>
    <Editor initialValue={content.rawContent} height="80vh" lass="" options={options} bind:this={editor}/>
</template>