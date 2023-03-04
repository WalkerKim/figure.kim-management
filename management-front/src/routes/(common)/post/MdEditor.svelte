<script>
    import CategoryChoice from "../category/CategoryChoice.svelte";
    import ContentProps from "./ContentProps.svelte";
    import {serverHost} from "$lib/common.js";
    import Editor from 'tui-editor-svelte/Editor.svelte';
    import '@toast-ui/editor/dist/toastui-editor-viewer.css';

    export let content;
    export let editor;
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
                    attributes:{
                        id: context.getChildrenText(node).trim().replace(/\s+/g, '-')
                    }
                }
            },
            codeBlock(node, context) {
                console.log("codeBlock",node, context)
                return [
                    { type: 'openTag', tagName: 'pre', classNames: [`language-${node.info}`,'line-numbers'] },
                    { type: 'openTag', tagName: 'code', classNames: [`language-${node.info}`] },
                    { type: 'text', content: node.literal },
                    { type: 'closeTag', tagName: 'code' },
                    { type: 'closeTag', tagName: 'pre' }
                ];
            },

        }

    }


</script>
<template>
    <div class="mb-5">
        <CategoryChoice bind:selectedCategoryIdArray={content.categoryIdList} isActiveAutoSelectParent={true}/>
    </div>
    <ContentProps bind:content={content}></ContentProps>
    <Editor initialValue={content.rawContent} height="80vh" lass="" options={options} bind:this={editor}/>
</template>