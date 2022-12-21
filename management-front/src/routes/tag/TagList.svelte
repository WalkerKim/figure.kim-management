<script>
    import Grid from "gridjs-svelte"
    import {deleteDataWithHost, fetchWithMethod, serverHost} from "$lib/common.js";
    import {h} from "gridjs";
    import "$lib/grid.css";
    import {goto} from "$app/navigation";

    let gridInstance;

    function getSettings() {
        const settings = {
            columns: [
                {id: "id", name: "id"},
                {
                    id: "contentList", name: "contentList", formatter: (cell, row) => {
                        return cell.map(content => content.title).join()
                    }
                },
                {
                    id: "contentList", name: "contentCount", formatter: (cell, row) => {
                        return cell.map(content => content.title).length
                    }
                },
                {
                    id: "id", name: "action", formatter: (cell, row) => {
                        return h('div', {className: 'inline-flex w-full', id: 'foo'},
                            h('button', {
                                className: "inline-flex mr-3 bg-green-900 hover:bg-green-700 border-green-400 text-green-400 px-2 py-1 rounded w-auto whitespace-nowrap",
                                onClick: () => {
                                    goto("/tag/" + cell)
                                }
                            }, "Content List"));
                    }
                },


            ],
            pagination: {
                enable: true,
                limit: 10,
                server: {
                    url: (prev, page, limit) => {
                        return `${prev}?limit=${limit}&offset=${page}`
                    }
                }
            },
            server: {
                url: "/tag",
                data: async (opts) => {
                    console.log(opts)
                    let contentListPromise = fetchWithMethod("GET", opts.url);
                    let countPromise = fetchWithMethod("GET", "/tag/count");
                    const contentList = await contentListPromise.then(res => res.json());
                    const count = await countPromise.then(res => res.json());
                    ;
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

            },
            style: {
                table: {
                    'width': '100%'
                }
            }

        }
        return settings;
    }

    let gridSettings = getSettings();
</script>
<template>
    <div class="container">
        <Grid bind:instance={gridInstance} {...gridSettings}></Grid>
    </div>
</template>