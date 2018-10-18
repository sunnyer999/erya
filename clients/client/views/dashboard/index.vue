<template>
  <div>
    <div class="tile is-ancestor">
      <div class="tile is-parent is-12">
        <article class="tile is-child box">
          <!-- This part need refactor to a nav-bar template below -->
          <section class="hero is-primary has-shadow" id="hero">
            <div class="hero-body" style="background: #4DD0E1">
              <div class="container">
                <h3 class="title">Pipeline Duration Statistics</h3>
                <h2 class="subtitle">Pipeline <strong>overview</strong> data</h2>
              </div>
            </div>

            <div class="hero-foot" style="background: #4DD0E1">
              <div class="container">
                <nav class="tabs is-boxed is-primary">
                  <ul>
                    <li class="" v-for="(pipeline, index) in pipelines" :class="[index == durationNavIndex? active : '']">
                      <a id="Overview" @click="getDurationStats(index, pipeline)">{{ pipeline.label }}</a>
                    </li>
                  </ul>
                </nav>
              </div>
            </div>
          </section>
          <div class="nav has-shadow">
            <div class="container">
              <div class="nav-left">
                <a class="nav-item is-tab is-active" v-for="item in items">{{ item }}</a>
              </div>
            </div>
          </div>
          <!-- This part need refactor to a nav-bar template above -->

          <highcharts :options="durationOption" ref="highcharts"></highcharts>
        </article>
      </div>
    </div>

    <div class="tile is-ancestor">
      <div class="tile is-parent is-12">
        <article class="tile is-child box">
          <!-- This part need refactor to a nav-bar template below -->
          <section class="hero is-primary has-shadow" id="hero">
            <div class="hero-body" style="background: #4DD0E1">
              <div class="container">
                <h3 class="title">Deployments Statistics</h3>
                <h2 class="subtitle">Pipeline <strong>overview</strong> data</h2>
              </div>
            </div>

            <div class="hero-foot" style="background: #4DD0E1">
              <div class="container">
                <nav class="tabs is-boxed is-primary">
                  <ul>
                    <li class="" v-for="(pipeline, index) in pipelines" :class="[index == deployNavIndex? active : '']">
                      <a @click="getDeployNavIndex(index)" :class="[index == deployNavIndex? active : '']">{{ pipeline.label }}</a>
                    </li>
                  </ul>
                </nav>
              </div>
            </div>
          </section>
          <div class="nav has-shadow">
            <div class="container">
              <div class="nav-left">
                <a :class="[index == deploySubNavIndex? subNavActive : subNavInActive]" v-for="(item, index) in deploySubNav" @click="getDeploySubNavIndex(index)">{{ item.label }}</a>
              </div>
            </div>
          </div>
          <!-- This part need refactor to a nav-bar template above -->

          <div class="notification" id="notification" v-if="deploySubNavIndex == 0 || deploySubNavIndex == 1">
            <el-date-picker v-model="dateTimePicker" type="daterange" placeholder="Select Time Scope" :picker-options="pickerOptions"
              format="yyyy-MM-dd"></el-date-picker>
          </div>

          <highcharts :options="deployOption" ref="highcharts" v-if="deploySubNavIndex == 0 || deploySubNavIndex == 1"></highcharts>

          <!-- This is deployment details table-->
          <div id="deploy-status-table" v-if="deploySubNavIndex == 2">
            <div class="deploy animated fadeIn">
              <el-table :data="deployStatusByPage" style="width: 100%">
                <el-table-column prop="index" label="" width="50" >
                  <template scope="scope">
                    <i class="fa fa-desktop fa-lg" aria-hidden="true"></i>
                  </template>
                </el-table-column>

                <el-table-column prop="appliance" label="Appliance" sortable>
                </el-table-column>

                <el-table-column prop="type" label="Type" sortable>
                </el-table-column>

                <el-table-column prop="status" label="Status" sortable>
                </el-table-column>

                <el-table-column prop="uri" label="Reason">
                  <template scope="scope">
                    <a class="icon" :href="scope.row.uri">
                      <i class="fa fa-info-circle fa-lg" aria-hidden="true"></i>
                    </a>
                    <a href="" class="icon" :href="scope.row.uri">
                      <i class="fa fa-share-alt fa-lg" aria-hidden="true"></i>
                    </a>
                  </template>
                </el-table-column>

                <el-table-column prop="" label="Usage frequency" sortable>
                </el-table-column>
              </el-table>
            </div>
            <div class="columns">
              <div class="column is-offset-4">
                <el-pagination layout="prev, pager, next" :total="total" @current-change="getDeployStatusByPage" style="border-radius: 0px"
                  :page-size="10"></el-pagination>
              </div>
            </div>
          </div>
        </article>
      </div>
    </div>

    <div class="tile is-ancestor">
      <div class="tile is-parent is-12">
        <article class="tile is-child box">

          <!-- This part need refactor to a nav-bar template below -->
          <section class="hero is-primary has-shadow" id="hero">
            <div class="hero-body" style="background: #4DD0E1">
              <div class="container">
                <h3 class="title">Test Scenario Statistics</h3>
                <h2 class="subtitle">Pipeline <strong>overview</strong> data</h2>
              </div>
            </div>

            <div class="hero-foot" style="background: #4DD0E1">
              <div class="container">
                <nav class="tabs is-boxed is-primary">
                  <ul>
                    <li class="" v-for="(pipeline, index) in pipelines" :class="[index == testNavIndex? active : '']">
                      <a @click="getTestNavIndex(index)" :class="[index == deployNavIndex? active : '']">{{ pipeline.label }}</a>
                    </li>
                  </ul>
                </nav>
              </div>
            </div>
          </section>
          <div class="nav has-shadow">
            <div class="container">
              <div class="nav-left">
                <a :class="[index == testSubNavIndex? subNavActive : subNavInActive]" v-for="(item, index) in testNav" @click="getTestSubNavIndex(index)">{{ item.label }}</a>
              </div>
            </div>
          </div>
          <!-- This part need refactor to a nav-bar template above -->
          <latest-test-scenario-stats v-if="testSubNavIndex==0" :title="currentTestPipeline.label" :pipeline-name="currentTestPipeline.name"></latest-test-scenario-stats>
          <test-scenario-stats v-if="testSubNavIndex==1" :title="currentTestPipeline.label" :pipeline-name="currentTestPipeline.name"></test-scenario-stats>
        </article>
      </div>
    </div>
  </div>
</template>


<script>
  import { mapGetters } from 'vuex'
  import moment from 'moment'
  import { TestScenarioStats, LatestTestScenarioStats } from './components'

  export default {
    data () {
      return {
        currentTestPipeline: { label: 'NBA Pipeline', name: 'nba_pipeline', deployName: 'nba_main_regression_deploy' },
        dateTimePicker: [moment().subtract(14, 'days'), moment()],
        pickerOptions: {
          shortcuts: [{
            text: 'Last week',
            onClick (picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          }, {
            text: 'Last 2 weeks',
            onClick (picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 14)
              picker.$emit('pick', [start, end])
            }
          }, {
            text: 'Last 3 weeks',
            onClick (picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 21)
              picker.$emit('pick', [start, end])
            }
          }, {
            text: 'All weeks',
            onClick (picker) {
              const end = new Date()
              const start = new Date(2017, 1, 7)
              picker.$emit('pick', [start, end])
            }
          }, {
            text: 'Last month',
            onClick (picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          }]
        },
        items: ['Overview'],
        pipelines: [
          { label: 'NBA Pipeline', name: 'nba_pipeline', deployName: 'nba_main_regression_deploy' },
          { label: 'NBA Long Regression', name: 'nba_long_regression_pipeline', deployName: 'nba_main_long_regression_deploy' },
          { label: 'Velocity Pipeline', name: 'velocity_pipeline', deployName: 'velocity_main_regression_deploy' }],
        active: 'is-active',
        subNavActive: 'nav-item is-tab is-active',
        subNavInActive: 'nav-item is-tab',
        durationNavIndex: 0,
        deploySubNav: [{ label: 'By-buildId', timeScopeType: 'triggerId' }, { label: 'By-week', timeScopeType: 'week' }, { label: 'Deploy-status' }],
        deployNavIndex: 0,
        deploySubNavIndex: 0,
        testNav: [{ label: 'Latest' }, { label: 'Overview' }],
        testNavIndex: 0,
        testSubNavIndex: 0
      }
    },

    watch: {
      dateTimePicker: function (val) {
        if (val && val[0]) {
          this.getDeployStats(this.deployNavIndex, this.deploySubNavIndex)
        }
      }
    },

    components: {
      TestScenarioStats,
      LatestTestScenarioStats
    },

    computed: {
      ...mapGetters({
        deployOption: 'deployOption',
        durationOption: 'durationOption',
        deployStatus: 'deployStatus',
        deployStatusByPage: 'deployStatusByPage',
        total: 'total'
      })
    },

    methods: {
      getDeployNavIndex (index) {
        this.deployNavIndex = index
        this.deploySubNavIndex = 0
        this.
        (index, 0)
      },

      getDeploySubNavIndex (index) {
        this.deploySubNavIndex = index
        if (index === 0 || index === 1) {
          this.getDeployStats(this.deployNavIndex, index)
        } else if (index === 2) {
          this.getDeployStatus()
        }
      },

      getDeployStats (navIndex, subNavIndex) {
        if (this.dateTimePicker) {
          this.dateBegin = moment(this.dateTimePicker[0]).format('YYYY-MM-DD')
          this.dateEnd = moment(this.dateTimePicker[1]).format('YYYY-MM-DD')
          this.$store.dispatch('getDeployStats',
            {
              'timeScopeType': this.deploySubNav[subNavIndex].timeScopeType,
              'deployPipeline': this.pipelines[navIndex].name,
              'deployName': this.pipelines[navIndex].deployName,
              'dateBegin': this.dateBegin,
              'dateEnd': this.dateEnd
            })
        }
      },

      getDeployStatus () {
        let currIndex = this.deployNavIndex
        let deployJobName = this.pipelines[currIndex].deployName
        this.$store.dispatch('getLatestDeployStatus', { 'deployJobName': deployJobName })
      },

      getDurationStats (index, pipeline) {
        this.durationNavIndex = index
        this.$store.dispatch('getDurationStats', { 'durationPipeline': pipeline.name })
      },

      getTestNavIndex (index) {
        this.testNavIndex = index
        this.testSubNavIndex = 0
        this.currentTestPipeline = this.pipelines[index]
      },

      getTestSubNavIndex (index) {
        this.testSubNavIndex = index
      },

      getDeployStatusByPage (page) {
        this.$store.dispatch('getLatestDeployStatusByPage', { 'page': page })
      }

    },

    created () {
      this.$store.dispatch('getDurationStats', { 'durationPipeline': this.pipelines[0].name })
      this.$store.dispatch('getDeployStats',
        {
          'timeScopeType': this.timeScopeType,
          'deployPipeline': this.pipelines[0].name,
          'deployName': this.pipelines[0].deployName,
          'dateBegin': this.dateTimePicker[0].format('YYYY-MM-DD'),
          'dateEnd': this.dateTimePicker[1].format('YYYY-MM-DD')
        })
    }
  }

</script>

<style lang="scss" scoped>
  .fa-tasks {
    color: #8492A6
  }

  .fa-area-chart {
    color: #8492A6
  }

  .fa-table {
    color: #8492A6
  }

  .fa-tags {
    color: #475669
  }

  .fa-address-book-o {
    color: #455a64
  }

  .flip-list-move {
    transition: transform 1s;
  }

  .hero-body {
    padding: 20px
  }

  .deploy {
    margin-top: 40px;
    margin-bottom: 20px
  }
</style>
