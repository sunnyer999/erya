<template>
  <div class="content has-text-centered">
    <p>
      <img width="200" src="~assets/logo.png" :alt="description">
    </p>

    <h1 class="is-title is-bold">{{ name.replace('-', ' ') }}</h1>

    <p>
      <strong>{{ description }}</strong>,
      <!--<a :href="homepage">Live Demo</a>-->
    </p>

    <p>Supports Vue 2.0 and Bulma 0.2!</p>
  </div>
</template>

<script>
export default {

  data () {
    return {
      description: 'Veritas corp',
      name: 'Automation Icebreaker'
    }
  }

}
</script>

<style lang="scss" scoped>
.is-title {
  text-transform: capitalize;
}
</style>
