import {error} from '@sveltejs/kit';
import {getDataWithHost} from "$lib/common.js";

/** @type {import('./$types').PageLoad} */
export async function load({ params }) {
    let result = await getDataWithHost("/content/" + params.id);
    result.categoryList = result.categoryList ?? [];
    result.categoryIdList = result.categoryList.map(category=>category.id);
    return result;
    throw error(404, 'Not found');
}