import CoreData
import SwiftUI

struct MainView: View {
    @State var project: Project?

    var body: some View {
        if project != nil {
            ProjectView(project: $project)
        } else {
            WelcomeView(project: $project)
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            NavigationView {
                MainView(project: nil)
            }
        }
    }
}
