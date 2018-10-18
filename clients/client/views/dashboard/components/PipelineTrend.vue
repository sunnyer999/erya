<template>
  <div>
    <highcharts :options="option" ref="highcharts"></highcharts>
  </div>
</template>
<script>
  import { mapGetters } from 'vuex'
  import _ from 'lodash'

  export default {
    props: {
      height: {
        type: Number,
        default: 315
      },
      builds: {
        type: Number,
        default: 5
      },
      pipelineName: {
        type: String,
        required: true
      }
    },

    methods: {
      getPipelineTrend (pipelineName, builds) {
        this.$store.dispatch('getPipelineRecentTrends', {'pipeline': pipelineName, 'builds': builds})
      }
    },

    computed: {
      ...mapGetters({
        trends: 'recentPipelineTrends'
      }),

      trend () {
        if (!this.trends) return {}
        return _.sortBy(this.trends[this.pipelineName], 'triggerId') || []
      },
      option () {
        return {
          chart: {
            type: 'areaspline',
            height: this.height
          },
          title: {
            text: 'Pass Rate Trend'
          },
          subtitle: {text: '(click point to select)'},
          legend: {
            enabled: false
          },
          xAxis: {
            categories: _.map(this.trend, t => t.triggerId),
            labels: {
              format: '#' + '{value}'
            }
          },
          yAxis: {
            min: 0,
            max: 100,
            title: {
              text: 'Pass Rates'
            },
            labels: {
              format: '{value}' + '%'
            }
          },
          tooltip: {
            headerFormat: '<span style="font-size: 10px">#{point.key} (click point to view detail)</span><br/>',
            enabled: true,
            valueSuffix: '%'
          },
          credits: {
            enabled: false
          },
          plotOptions: {
            areaspline: {
              color: '#4dd0e1',
              fillOpacity: 0.5
            },
            dataLabels: {enabled: false}
          },
          series: [{
            name: 'Pass Rate',
            data: _.map(this.trend, t => Math.floor((t.totalPassedCases / t.totalExpectedCases) * 100)),
            events: {
              click: (e) => {
                this.$emit('triggerIdSelected', e.point.category)
              }
            }
          }]
        }
      }
    },

    watch: {
      pipelineName () {
        this.getPipelineTrend(this.pipelineName, this.builds)
      }
    },

    created () {
      this.getPipelineTrend(this.pipelineName, this.builds)
    }

  }

</script>

<style>
</style>
