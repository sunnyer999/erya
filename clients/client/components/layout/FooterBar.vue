<template>
  <footer class="footer">
    <div class="container">
      <div class="content has-text-centered">
        <p class="social">
          <a href="https://stash.veritas.com/ projects">
            <span class="icon">
              <i class="fa fa-bitbucket"></i>
            </span>
          </a>
          <a href="https://www.veritas.com/ ">
            <span class="icon">
              <i class="fa fa-paper-plane"></i>
            </span>
          </a>
        </p>
        <p><span class="icon"><i class="fa fa-code"></i></span> with <span class="icon"><i class="fa fa-heart"></i></span> by
          <a href="https://stash.veritas.com/users/dean.zou/repos/erya/browse/">Dean.Zou</a> , 
          <a href="https://stash.veritas.com/users/qihan.luo/repos/async_jenkins_crawler/browse">Qihan.Luo</a> & 
          <a href="https://stash.veritas.com/users/dean.zou/repos/erya/browse/">Sunny.Zhao</a>.          
        </p>
        <p>Code licensed under <a :href="'https://github.com/' + repository + '/blob/master/LICENSE'">{{ license }}</a>.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  data () {
    return this.$store.state.pkg
  }
}
</script>

<style lang="scss">
  @import '~bulma/sass/utilities/mixins';
  .footer {
    margin-left: 180px;
    @include mobile() {
      margin-left: 0;
    }
    .social a {
      border-bottom: none !important;
    }
    .fa.fa-heart {
      color: red;
    }
  }
</style>
