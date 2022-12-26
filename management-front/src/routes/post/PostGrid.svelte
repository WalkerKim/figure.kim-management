<script>
    export let url;
    import Grid from "gridjs-svelte"
    import {h} from "gridjs"
    import "$lib/grid.css";
    import {deleteDataWithHost, fetchWithMethod} from "$lib/common.js";
    import {goto} from "$app/navigation";


    let gridInstance;

    function getSettings() {
        const settings = {
            width: "auto",
            columns: [
                {
                    id: "createdAt", formatter: (cell, row) => new Date(cell).toLocaleString()
                    ,
                    name: "createAt"
                },
                {id: "id", name: "id"},
                {id: "title", name: "title"},
                {id: "description", name: "description"},
                {id: "isDraft", name: "isDraft", formatter: cell => cell ? "true" : "false"},
                {id: "isPublished", name: "isPublished", formatter: cell => cell ? "true" : "false"},
                {
                    id: "tagList", name: "tagList", formatter: (cell, row) => {
                        return Array.isArray(cell) ? cell.map(i => i.id).join(",") : cell;
                    }
                },
                {
                    id: "categoryList", name: "Category", formatter: (cell, row) => {
                        return Array.isArray(cell) ? cell.map(i => i.id).join(",") : cell;
                    }
                },
                {id: "lastModifiedAt", name: "lastModifiedAt", formatter: cell => new Date(cell).toLocaleString()},
                {
                    id: "id", name: "action", formatter: (cell, row) => {
                        console.log(cell)
                        return h('div', {className: 'inline-flex w-full', id: 'foo'},
                            h('button', {
                                className: "inline-flex mr-3 bg-green-900 hover:bg-green-700 border-green-400 text-green-400 px-2 py-1 rounded w-auto whitespace-nowrap",
                                onClick: () => {
                                    goto("/post/edit/" + cell)
                                }
                            }, "Edit"),
                            h('button', {
                                className: "hidden sm:inline-flex bg-red-500 hover:bg-red-600 border-red-500 text-white px-2  py-1 rounded w-auto whitespace-nowrap",
                                onClick: () => {
                                    if (confirm("삭제시 복구할 수 없습니다. 삭제하시겠습니까?")) {
                                        deleteDataWithHost("/content/" + cell, null, true).then(() => {
                                            if (gridInstance) {
                                                gridInstance.updateConfig(getSettings()).forceRender()
                                            }
                                        })
                                    }
                                }
                            }, "Delete"));
                    }
                },


            ],
            pagination: {
                enable: true,
                limit: 5,
                server: {
                    url: (prev, page, limit) => {
                        return `${prev}?limit=${limit}&offset=${page}`
                    }
                }
            },
            server: {
                url: url,
                data: async (opts) => {
                    console.log(opts)
                    let contentListPromise = fetchWithMethod("GET", opts.url);
                    let countPromise = fetchWithMethod("GET", url + "/count");
                    const contentList = await contentListPromise.then(res => res.json());
                    const count = await countPromise.then(res => res.json());
                    let result = {};
                    result["data"] = contentList;
                    result["total"] = count;
                    return result;
                },
                credentials: "include",
                // total: data => data.count
            },
            className: {
                table: 'bg-custom-black-900',
                tbody: 'bg-custom-black-900',
                container: 'mt-0 pt-0',
                paginationSummary: 'hidden sm:inline',
                search: 'w-full sm:w-auto'
            }

        }
        return settings;
    }

    let gridSettings = getSettings();
</script>
<template>
    <Grid bind:instance={gridInstance} {...gridSettings}></Grid>
</template>