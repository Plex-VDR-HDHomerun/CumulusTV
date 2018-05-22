package com.felkertech.cumulustv.model;

import android.media.tv.TvContract;

import com.felkertech.cumulustv.plugins.CumulusChannel;

/**
 * <p>This utility class is a single place to access the "Suggested Channels" that are seen in the
 * app. Having them in a separate place makes it easy to edit without affecting other parts
 * of the app.
 *
 * <p>The numbering scheme for these channels is a bit specific. They follow a rule:
 * </p>
 *
 * <ul>
 * <li>0xx NEWS</li>
 * <li>1xx SCIENCE / TECH / NATURE</li>
 * <li>2xx HISTORY / EDUCATION</li>
 * <li>3xx SPORTS / VIDEO GAMES</li>
 * <li>4xx MUSIC</li>
 * <li>5xx FICTION</li>
 * <li>6xx NONFICTION</li>
 * <li>7xx GOVERNMENT / SOCIETY</li>
 * <li>8xx ART / CULTURE / LANGUAGE</li>
 * <li>9xx MISC</li>
 * </ul>
 *
 * <p>
 * Some streams were found via <a href='http://rgw.ustream.tv/json.php/Ustream.searchBroadcast/'>
 *     UStream</a>.</p>
 */
public class SuggestedChannels {
    private static final CumulusChannel[] channels = {
            new JsonChannel.Builder()
                    .setGenres(TvContract.Programs.Genres.TECH_SCIENCE + "," +
                            TvContract.Programs.Genres.NEWS)
                    .setLogo("http://tmsimg.plex.tv/h3/NowShowing/58646/s58646_h3_aa.png")
                    .setMediaUrl("http://192.168.1.219:5004/auto/v600")
                    .setName("CNN HD")
                    .setNumber("600")
                    .build(),
            new JsonChannel.Builder()
                    .setGenres(TvContract.Programs.Genres.TECH_SCIENCE + "," +
                            TvContract.Programs.Genres.NEWS)
                    .setLogo("http://tmsimg.plex.tv/h3/NowShowing/28717/s28717_h3_aa.png")
                    .setMediaUrl("http://plexlivetv.ddns.net:32400/livetv/sessions/b7451b12-5bc0-46cd-ba30-a2418cc58778/5d741bxttfbo214ga0nl1a14/index.m3u8?X-Plex-Token=2DA7WyFpTpuQzK7qRpDJ")
                    .setName("NBC HD")
                    .setNumber("508")
                    .build(),
            new JsonChannel.Builder()
                    .setGenres(TvContract.Programs.Genres.TECH_SCIENCE + "," +
                            TvContract.Programs.Genres.NEWS)
                    .setLogo("http://tmsimg.plex.tv/h3/NowShowing/56905/s56905_h3_aa.png")
                    .setMediaUrl("http://plexlivetv.ddns.net:3000/I-0-410-632")
                    .setName("DISCHD")
                    .setNumber("1")
                    .build(),
            new JsonChannel.Builder()
                    .setGenres(TvContract.Programs.Genres.TECH_SCIENCE + "," +
                            TvContract.Programs.Genres.NEWS)
                    .setLogo("http://tmsimg.plex.tv/h3/NowShowing/49438/s49438_h3_aa.png")
                    .setMediaUrl("http://192.168.1.219:5004/auto/v621")
                    .setName("NATGEO")
                    .setNumber("621")
                    .build(),
            new JsonChannel.Builder()
                    .setGenres(TvContract.Programs.Genres.TECH_SCIENCE + "," +
                            TvContract.Programs.Genres.NEWS)
                    .setLogo("http://tmsimg.plex.tv/h3/NowShowing/57708/s57708_h3_aa.png")
                    .setMediaUrl("http://192.168.1.219:5004/auto/v628")
                    .setName("HSTRYHD")
                    .setNumber("628")
                    .build(),
            new JsonChannel.Builder()
                    .setGenres(TvContract.Programs.Genres.ARTS + "," +
                            TvContract.Programs.Genres.ENTERTAINMENT)
                    .setLogo("http://tmsimg.plex.tv/h3/NowShowing/45438/s45438_h3_aa.png")
                    .setMediaUrl("http://192.168.1.219:5004/auto/v669")
                    .setName("AWEHD")
                    .setNumber("669")
                    .build()
    };

    public static CumulusChannel[] getSuggestedChannels() {
        return channels;
    }
}