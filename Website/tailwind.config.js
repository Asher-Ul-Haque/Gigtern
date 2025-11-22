/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      colors: {
        royalViolet: '#6A0DAD', 
        royalVioletHover: '#7D1EBC',
      },
    },
  },
  plugins: [],
};
