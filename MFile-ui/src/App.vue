<template>
  <div id="app" ref="app">
    <!-- <router-view /> -->
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive"></router-view>
  </div>
</template>
<script>
export default {
  name: 'App',
  methods: {},
  computed: {
    key() {
      return this.$route.path
    }
  },
  mounted() {}
}
</script>
<style>
#app {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>
