<template>
  <div>
    <!-- Main container -->
    <nav class="level">
      <div class="level-left">
        <div class="level-item animated flipInX">
          <el-cascader :options="options" change-on-select></el-cascader>
        </div>
      </div>
    </nav>
    <div v-for="req in reqs">
      <app-req :appReq="req"></app-req>
      <br>
    </div>
  </div>
</template>

<script>
  import {AppReq} from './'

  export default {
    components: {
      AppReq
    },
    props: {
      reqs: {
        type: Array,
        default: () => {
          return []
        }
      }
    },

    data () {
      return {
        options: [{
          value: 'pi7',
          label: 'PI 7',
          children: [{
            value: '5220',
            label: '5220',
            children: [{
              value: 'all',
              label: 'All'
            }, {
              value: 'icebreaker',
              label: 'Icebreaker'
            }, {
              value: 'autobots',
              label: 'Autobots'
            }]
          }, {
            value: '5230',
            label: '5230',
            children: [{
              value: 'all',
              label: 'All'
            }, {
              value: 'icebreaker',
              label: 'Icebreaker'
            }, {
              value: 'autobots',
              label: 'Autobots'
            }]
          }]
        }, {
          value: 'pi8',
          label: 'PI 8',
          children: [{
            value: '5220',
            label: '5220',
            children: [{
              value: 'all',
              label: 'All'
            }, {
              value: 'icebreaker',
              label: 'Icebreaker'
            }, {
              value: 'autobots',
              label: 'Autobots'
            }]
          }, {
            value: '5230',
            label: '5230',
            children: [{
              value: 'all',
              label: 'All'
            }, {
              value: 'icebreaker',
              label: 'Icebreaker'
            }, {
              value: 'autobots',
              label: 'Autobots'
            }]
          }]
        }, {
          value: 'pi9',
          label: 'PI 9',
          children: [{
            value: 'all',
            label: 'All'
          }, {
            value: '5220',
            label: '5220',
            children: [{
              value: 'icebreaker',
              label: 'Icebreaker'
            }, {
              value: 'autobots',
              label: 'Autobots'
            }]
          }, {
            value: '5230',
            label: '5230',
            children: [{
              value: 'all',
              label: 'All'
            }, {
              value: 'icebreaker',
              label: 'Icebreaker'
            }, {
              value: 'autobots',
              label: 'Autobots'
            }]
          }]
        }]
      }
    }

  }
</script>

<style lang="scss" scoped>
  @import '~element-ui/lib/theme-default/index.css'
</style>
