/** @type {import('tailwindcss').Config} */

// const { colors: defaultColors } = require('tailwindcss/defaultTheme')
// const defaultTheme = require('tailwindcss/defaultTheme')
const defaultColors = require('tailwindcss/colors')

delete defaultColors.lightBlue;
delete defaultColors.warmGray;
delete defaultColors.trueGray;
delete defaultColors.coolGray;
delete defaultColors.blueGray;

const colors = {
  ...defaultColors
  ,
  ...{
    "green": {
      "100": "#A5EEC5",
      "200": "#5DE097",
      "300": "#39D980",
      "400": "#03CF5D",
      "500": "#0AB456",
      "600": "#13924D",
      "700": "#1C7145",
      "800": "#254F3C",
      "900": "#2A3B37",
    },
    "blue": {
      "100": "#DCEDFF",
      "200": "#98CBFF",
      "300": "#71B8FF",
      "400": "#3197FF",
      "500": "#2387ee",
      "600": "#1076DD",
      "700": "#1863B3",
      "800": "#1F5188",
      "900": "#2B3444",
    },"red": {
      "100": "#FDE0E0",
      "200": "#FCC2C2",
      "300": "#FA9999",
      "400": "#F76666",
      "500": "#F53D3D",
      "600": "#BC383A",
      "700": "#753337",
      "800": "#4A2F34",
      "900": "#382E34",
    },"gray": {
      "100": "#E9E9EC",
      "200": "#C1BFC7",
      "300": "#ACABB4",
      "400": "#9896A2",
      "500": "#838190",
      "600": "#666472",
      "700": "#54525D",
      "800": "#413F48",
      "900": "#2E2D33",
    },"custom-black": {
      "100": "#f5f5f7",
      "900": "#1D1C22"
    }
  }
}

module.exports = {
  content: ['./src/**/*.{html,js,svelte,ts}'],
  theme: {
    colors: colors,
    extend: {},
  },
  plugins: [],
}
